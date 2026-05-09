package com.servicebooking.controller;

import com.servicebooking.dto.ApiResponse;
import com.servicebooking.dto.BookingRequest;
import com.servicebooking.model.Booking;
import com.servicebooking.model.BookingStatus;
import com.servicebooking.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "Bookings", description = "APIs for managing service bookings and appointments")
public class BookingController {

    @Autowired
    BookingService bookingService;

    @GetMapping("/customer")
    @Operation(summary = "Get customer bookings", description = "Retrieves all bookings for the authenticated customer")
    @SecurityRequirement(name = "bearer-jwt")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Bookings retrieved successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad request or error occurred"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ApiResponse<List<Booking>>> getCustomerBookings(Authentication authentication) {
        try {
            List<Booking> bookings = bookingService.getCustomerBookings(authentication.getName());
            return ResponseEntity.ok(ApiResponse.success(bookings));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.<List<Booking>>error(e.getMessage()));
        }
    }

    @GetMapping("/provider")
    @Operation(summary = "Get provider bookings", description = "Retrieves all bookings for the authenticated service provider")
    @SecurityRequirement(name = "bearer-jwt")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Bookings retrieved successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad request or error occurred"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ApiResponse<List<Booking>>> getProviderBookings(Authentication authentication) {
        try {
            List<Booking> bookings = bookingService.getProviderBookings(authentication.getName());
            return ResponseEntity.ok(ApiResponse.success(bookings));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.<List<Booking>>error(e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get booking by ID", description = "Retrieves a specific booking by its ID")
    @SecurityRequirement(name = "bearer-jwt")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Booking retrieved successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad request or error occurred"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ApiResponse<Booking>> getBookingById(@Parameter(description = "Booking ID") @PathVariable String id) {
        try {
            Booking booking = bookingService.getBookingById(id);
            return ResponseEntity.ok(ApiResponse.success(booking));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.<Booking>error(e.getMessage()));
        }
    }

    @PostMapping
    @Operation(summary = "Create booking", description = "Creates a new service booking")
    @SecurityRequirement(name = "bearer-jwt")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Booking created successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad request or error occurred"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ApiResponse<Booking>> createBooking(
            @Valid @RequestBody BookingRequest request,
            Authentication authentication) {
        try {
            Booking booking = bookingService.createBooking(request, authentication);
            return ResponseEntity.ok(ApiResponse.success(booking, "Booking created successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.<Booking>error(e.getMessage()));
        }
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update booking status", description = "Updates the status of a booking")
    @SecurityRequirement(name = "bearer-jwt")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Booking status updated successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad request or error occurred"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ApiResponse<Booking>> updateBookingStatus(
            @Parameter(description = "Booking ID") @PathVariable String id,
            @Parameter(description = "New booking status") @RequestParam BookingStatus status,
            Authentication authentication) {
        try {
            Booking booking = bookingService.updateBookingStatus(id, status);
            return ResponseEntity.ok(ApiResponse.success(booking, "Booking status updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.<Booking>error(e.getMessage()));
        }
    }

    @PostMapping("/{id}/cancel")
    @Operation(summary = "Cancel booking", description = "Cancels a booking")
    @SecurityRequirement(name = "bearer-jwt")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Booking cancelled successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad request or error occurred"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ApiResponse<Booking>> cancelBooking(
            @Parameter(description = "Booking ID") @PathVariable String id,
            Authentication authentication) {
        try {
            Booking booking = bookingService.cancelBooking(id);
            return ResponseEntity.ok(ApiResponse.success(booking, "Booking cancelled successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.<Booking>error(e.getMessage()));
        }
    }
}
