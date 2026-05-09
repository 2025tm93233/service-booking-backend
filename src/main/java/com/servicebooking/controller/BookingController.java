package com.servicebooking.controller;

import com.servicebooking.dto.ApiResponse;
import com.servicebooking.dto.BookingRequest;
import com.servicebooking.model.Booking;
import com.servicebooking.model.BookingStatus;
import com.servicebooking.service.BookingService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "http://localhost:5173")
public class BookingController {

    @Autowired
    BookingService bookingService;

    @GetMapping("/customer")
    public ResponseEntity<ApiResponse<List<Booking>>> getCustomerBookings(Authentication authentication) {
        try {
            List<Booking> bookings = bookingService.getCustomerBookings(authentication.getName());
            return ResponseEntity.ok(ApiResponse.success(bookings));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.<List<Booking>>error(e.getMessage()));
        }
    }

    @GetMapping("/provider")
    public ResponseEntity<ApiResponse<List<Booking>>> getProviderBookings(Authentication authentication) {
        try {
            List<Booking> bookings = bookingService.getProviderBookings(authentication.getName());
            return ResponseEntity.ok(ApiResponse.success(bookings));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.<List<Booking>>error(e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Booking>> getBookingById(@PathVariable String id) {
        try {
            Booking booking = bookingService.getBookingById(id);
            return ResponseEntity.ok(ApiResponse.success(booking));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.<Booking>error(e.getMessage()));
        }
    }

    @PostMapping
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
    public ResponseEntity<ApiResponse<Booking>> updateBookingStatus(
            @PathVariable String id,
            @RequestParam BookingStatus status,
            Authentication authentication) {
        try {
            Booking booking = bookingService.updateBookingStatus(id, status);
            return ResponseEntity.ok(ApiResponse.success(booking, "Booking status updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.<Booking>error(e.getMessage()));
        }
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<Booking>> cancelBooking(
            @PathVariable String id,
            Authentication authentication) {
        try {
            Booking booking = bookingService.cancelBooking(id);
            return ResponseEntity.ok(ApiResponse.success(booking, "Booking cancelled successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.<Booking>error(e.getMessage()));
        }
    }
}
