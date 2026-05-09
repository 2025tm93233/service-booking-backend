package com.servicebooking.service;

import com.servicebooking.dto.ReviewRequest;
import com.servicebooking.model.Booking;
import com.servicebooking.model.BookingStatus;
import com.servicebooking.model.Review;
import com.servicebooking.model.Service;
import com.servicebooking.model.User;
import com.servicebooking.repository.BookingRepository;
import com.servicebooking.repository.ReviewRepository;
import com.servicebooking.repository.ServiceRepository;
import com.servicebooking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

import java.util.List;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ReviewService {

    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    ServiceRepository serviceRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    BookingRepository bookingRepository;

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public List<Review> getServiceReviews(String serviceId) {
        return reviewRepository.findByServiceId(serviceId);
    }

    public List<Review> getUserReviews(String userId) {
        return reviewRepository.findByUserId(userId);
    }

    public Review createReview(ReviewRequest request, Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Service service = serviceRepository.findById(request.getServiceId())
                .orElseThrow(() -> new RuntimeException("Service not found"));

        // Check if user has completed a booking for this service
        List<Booking> bookings = bookingRepository.findByServiceIdAndId(
                request.getServiceId(), user.getId());
        boolean hasCompletedBooking = bookings.stream()
                .anyMatch(b -> b.getStatus() == BookingStatus.COMPLETED);

        if (!hasCompletedBooking) {
            throw new RuntimeException("You can only review services you have booked and completed");
        }

        Review review = Review.builder()
                .serviceId(service.getId())
                .service(service)
                .userId(user.getId())
                .user(user)
                .rating(request.getRating())
                .comment(request.getComment())
                .build();

        return reviewRepository.save(review);
    }

    public Review updateReview(String id, ReviewRequest request) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        review.setRating(request.getRating());
        review.setComment(request.getComment());

        return reviewRepository.save(review);
    }

    public void deleteReview(String id) {
        reviewRepository.deleteById(id);
    }
}
