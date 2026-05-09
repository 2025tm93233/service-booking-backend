package com.servicebooking.repository;

import com.servicebooking.model.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepository extends MongoRepository<Service, String> {

    List<Service> findByProviderId(String providerId);

    List<Service> findByCategoryId(String categoryId);

    List<Service> findByIsActiveTrue();

    Page<Service> findByIsActiveTrue(Pageable pageable);
}
