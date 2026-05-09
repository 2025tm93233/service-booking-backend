package com.servicebooking.controller;

import com.servicebooking.dto.ApiResponse;
import com.servicebooking.model.Service;
import com.servicebooking.service.ServiceService;
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
public class ServiceController {

    @Autowired
    ServiceService serviceService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Service>>> getAllServices(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int limit,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortOrder) {

        Sort sort = Sort.by(Sort.Direction.fromString(sortOrder != null ? sortOrder : "DESC"), sortBy != null ? sortBy : "createdAt");
        Pageable pageable = PageRequest.of(page, limit, sort);
        Page<Service> services = serviceService.getServices(pageable);

        return ResponseEntity.ok(ApiResponse.success(services.getContent()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Service>> getServiceById(@PathVariable String id) {
        try {
            Service service = serviceService.getServiceById(id);
            return ResponseEntity.ok(ApiResponse.success(service));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.<Service>error(e.getMessage()));
        }
    }

    @GetMapping("/provider/{providerId}")
    public ResponseEntity<ApiResponse<List<Service>>> getServicesByProvider(@PathVariable String providerId) {
        List<Service> services = serviceService.getServicesByProvider(providerId);
        return ResponseEntity.ok(ApiResponse.success(services));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Service>> createService(
            @RequestBody Service service,
            Authentication authentication) {
        try {
            Service createdService = serviceService.createService(service);
            return ResponseEntity.ok(ApiResponse.success(createdService, "Service created successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.<Service>error(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Service>> updateService(
            @PathVariable String id,
            @RequestBody Service service,
            Authentication authentication) {
        try {
            Service updatedService = serviceService.updateService(id, service);
            return ResponseEntity.ok(ApiResponse.success(updatedService, "Service updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.<Service>error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteService(
            @PathVariable String id,
            Authentication authentication) {
        try {
            serviceService.deleteService(id);
            return ResponseEntity.ok(ApiResponse.success(null, "Service deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.<Void>error(e.getMessage()));
        }
    }
}
