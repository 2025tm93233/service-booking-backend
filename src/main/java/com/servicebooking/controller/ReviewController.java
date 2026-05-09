package com.servicebooking.controller;

import com.servicebooking.dto.ApiResponse;
import com.servicebooking.dto.ReviewRequest;
import com.servicebooking.model.Review;
import com.servicebooking.service.ReviewService;
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
public class ReviewController {

    @Autowired
    ReviewService reviewService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Review>>> getAllReviews() {
        List<Review> reviews = reviewService.getAllReviews();
        return ResponseEntity.ok(ApiResponse.success(reviews));
    }

    @GetMapping("/service/{serviceId}")
    public ResponseEntity<ApiResponse<List<Review>>> getServiceReviews(@PathVariable String serviceId) {
        List<Review> reviews = reviewService.getServiceReviews(serviceId);
        return ResponseEntity.ok(ApiResponse.success(reviews));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<Review>>> getUserReviews(@PathVariable String userId) {
        List<Review> reviews = reviewService.getUserReviews(userId);
        return ResponseEntity.ok(ApiResponse.success(reviews));
    }

    @GetMapping("/service/{serviceId}/average")
    public ResponseEntity<ApiResponse<Object>> getServiceAverageRating(@PathVariable String serviceId) {
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
    public ResponseEntity<ApiResponse<Review>> updateReview(
            @PathVariable String id,
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
    public ResponseEntity<ApiResponse<Void>> deleteReview(
            @PathVariable String id,
            Authentication authentication) {
        try {
            reviewService.deleteReview(id);
            return ResponseEntity.ok(ApiResponse.success(null, "Review deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.<Void>error(e.getMessage()));
        }
    }
}
