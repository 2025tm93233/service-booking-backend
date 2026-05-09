package com.servicebooking.controller;

import com.servicebooking.dto.ApiResponse;
import com.servicebooking.dto.ReviewRequest;
import com.servicebooking.model.Review;
import com.servicebooking.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "Reviews", description = "APIs for managing service reviews and ratings")
public class ReviewController {

    @Autowired
    ReviewService reviewService;

    @GetMapping
    @Operation(summary = "Get all reviews", description = "Retrieves all reviews in the system")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Reviews retrieved successfully")
    })
    public ResponseEntity<ApiResponse<List<Review>>> getAllReviews() {
        List<Review> reviews = reviewService.getAllReviews();
        return ResponseEntity.ok(ApiResponse.success(reviews));
    }

    @GetMapping("/service/{serviceId}")
    @Operation(summary = "Get service reviews", description = "Retrieves all reviews for a specific service")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Service reviews retrieved successfully")
    })
    public ResponseEntity<ApiResponse<List<Review>>> getServiceReviews(@Parameter(description = "Service ID") @PathVariable String serviceId) {
        List<Review> reviews = reviewService.getServiceReviews(serviceId);
        return ResponseEntity.ok(ApiResponse.success(reviews));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get user reviews", description = "Retrieves all reviews written by a specific user")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User reviews retrieved successfully")
    })
    public ResponseEntity<ApiResponse<List<Review>>> getUserReviews(@Parameter(description = "User ID") @PathVariable String userId) {
        List<Review> reviews = reviewService.getUserReviews(userId);
        return ResponseEntity.ok(ApiResponse.success(reviews));
    }

    @GetMapping("/service/{serviceId}/average")
    @Operation(summary = "Get service average rating", description = "Retrieves the average rating and review count for a specific service")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Average rating retrieved successfully")
    })
    public ResponseEntity<ApiResponse<Object>> getServiceAverageRating(@Parameter(description = "Service ID") @PathVariable String serviceId) {
        List<Review> reviews = reviewService.getServiceReviews(serviceId);
        double averageRating = reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);
        averageRating = this.round(averageRating, 1);
        Map response = Map.of("averageRating",averageRating, "reviewCount",reviews.size());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    private double round(double value, int places) {
        double scale = Math.pow(10, places);
        return Math.round(value * scale) / scale;
    }

    @PostMapping
    @Operation(summary = "Create review", description = "Creates a new review for a service")
    @SecurityRequirement(name = "bearer-jwt")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Review created successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad request or error occurred"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ApiResponse<Review>> createReview(
            @Valid @RequestBody ReviewRequest request,
            Authentication authentication) {
        try {
            Review review = reviewService.createReview(request, authentication);
            return ResponseEntity.ok(ApiResponse.success(review, "Review created successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.<Review>error(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update review", description = "Updates an existing review")
    @SecurityRequirement(name = "bearer-jwt")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Review updated successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad request or error occurred"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ApiResponse<Review>> updateReview(
            @Parameter(description = "Review ID") @PathVariable String id,
            @Valid @RequestBody ReviewRequest request,
            Authentication authentication) {
        try {
            Review review = reviewService.updateReview(id, request);
            return ResponseEntity.ok(ApiResponse.success(review, "Review updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.<Review>error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete review", description = "Deletes a review")
    @SecurityRequirement(name = "bearer-jwt")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Review deleted successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad request or error occurred"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ApiResponse<Void>> deleteReview(
            @Parameter(description = "Review ID") @PathVariable String id,
            Authentication authentication) {
        try {
            reviewService.deleteReview(id);
            return ResponseEntity.ok(ApiResponse.success(null, "Review deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.<Void>error(e.getMessage()));
        }
    }
}
