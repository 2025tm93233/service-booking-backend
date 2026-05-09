package com.servicebooking.repository;

import com.servicebooking.model.Booking;
import com.servicebooking.model.BookingStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends MongoRepository<Booking, String> {

    List<Booking> findByCustomerId(String customerId);

    List<Booking> findByProviderId(String providerId);

    List<Booking> findByCustomerIdAndStatus(String customerId, BookingStatus status);

    List<Booking> findByProviderIdAndStatus(String providerId, BookingStatus status);

    List<Booking> findByServiceId(String serviceId);

    List<Booking> findByServiceIdAndId(String serviceId, String id);
}
