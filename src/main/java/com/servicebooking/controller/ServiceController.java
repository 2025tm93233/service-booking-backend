package com.servicebooking.controller;

import com.servicebooking.dto.ApiResponse;
import com.servicebooking.model.Service;
import com.servicebooking.service.ServiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "Services", description = "APIs for managing service listings and provider services")
public class ServiceController {

    @Autowired
    ServiceService serviceService;

    @Operation(summary = "Get all services", description = "Retrieve paginated list of all active services with optional sorting")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Services retrieved successfully", content = @Content(schema = @Schema(implementation = Service.class)))
    })
    @GetMapping
    public ResponseEntity<ApiResponse<List<Service>>> getAllServices(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of items per page") @RequestParam(defaultValue = "12") int limit,
            @Parameter(description = "Field to sort by") @RequestParam(required = false) String sortBy,
            @Parameter(description = "Sort order (ASC or DESC)") @RequestParam(required = false) String sortOrder) {

        Sort sort = Sort.by(Sort.Direction.fromString(sortOrder != null ? sortOrder : "DESC"), sortBy != null ? sortBy : "createdAt");
        Pageable pageable = PageRequest.of(page, limit, sort);
        Page<Service> services = serviceService.getServices(pageable);

        return ResponseEntity.ok(ApiResponse.success(services.getContent()));
    }

    @Operation(summary = "Get service by ID", description = "Retrieve a specific service by its ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Service retrieved successfully", content = @Content(schema = @Schema(implementation = Service.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Service not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Service>> getServiceById(@Parameter(description = "Service ID") @PathVariable String id) {
        try {
            Service service = serviceService.getServiceById(id);
            return ResponseEntity.ok(ApiResponse.success(service));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.<Service>error(e.getMessage()));
        }
    }

    @Operation(summary = "Get services by provider", description = "Retrieve all services offered by a specific provider")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Services retrieved successfully", content = @Content(schema = @Schema(implementation = Service.class)))
    })
    @GetMapping("/provider/{providerId}")
    public ResponseEntity<ApiResponse<List<Service>>> getServicesByProvider(@Parameter(description = "Provider ID") @PathVariable String providerId) {
        List<Service> services = serviceService.getServicesByProvider(providerId);
        return ResponseEntity.ok(ApiResponse.success(services));
    }

    @Operation(summary = "Create service", description = "Create a new service listing (requires authentication)")
    @SecurityRequirement(name = "bearer-jwt")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Service created successfully", content = @Content(schema = @Schema(implementation = Service.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping
    public ResponseEntity<ApiResponse<Service>> createService(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Service details", required = true, content = @Content(schema = @Schema(implementation = Service.class))) @RequestBody Service service,
            Authentication authentication) {
        try {
            Service createdService = serviceService.createService(service);
            return ResponseEntity.ok(ApiResponse.success(createdService, "Service created successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.<Service>error(e.getMessage()));
        }
    }

    @Operation(summary = "Update service", description = "Update an existing service (requires authentication)")
    @SecurityRequirement(name = "bearer-jwt")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Service updated successfully", content = @Content(schema = @Schema(implementation = Service.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Service not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Service>> updateService(
            @Parameter(description = "Service ID") @PathVariable String id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Updated service details", required = true, content = @Content(schema = @Schema(implementation = Service.class))) @RequestBody Service service,
            Authentication authentication) {
        try {
            Service updatedService = serviceService.updateService(id, service);
            return ResponseEntity.ok(ApiResponse.success(updatedService, "Service updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.<Service>error(e.getMessage()));
        }
    }

    @Operation(summary = "Delete service", description = "Soft delete a service (requires authentication)")
    @SecurityRequirement(name = "bearer-jwt")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Service deleted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Service not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteService(
            @Parameter(description = "Service ID") @PathVariable String id,
            Authentication authentication) {
        try {
            serviceService.deleteService(id);
            return ResponseEntity.ok(ApiResponse.success(null, "Service deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.<Void>error(e.getMessage()));
        }
    }
}
