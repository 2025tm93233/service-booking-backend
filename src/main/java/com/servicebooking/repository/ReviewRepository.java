package com.servicebooking.repository;

import com.servicebooking.model.Review;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends MongoRepository<Review, String> {

    List<Review> findByServiceId(String serviceId);

    List<Review> findByUserId(String userId);

    boolean existsByServiceIdAndUserId(String serviceId, String userId);
}
