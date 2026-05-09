package com.servicebooking.service;

import com.servicebooking.dto.BookingRequest;
import com.servicebooking.model.Booking;
import com.servicebooking.model.BookingStatus;
import com.servicebooking.model.Service;
import com.servicebooking.model.TimeSlot;
import com.servicebooking.model.User;
import com.servicebooking.repository.BookingRepository;
import com.servicebooking.repository.ServiceRepository;
import com.servicebooking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

import java.util.List;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class BookingService {

    @Autowired
    BookingRepository bookingRepository;
    @Autowired
    ServiceRepository serviceRepository;
    @Autowired
    UserRepository userRepository;

    public List<Booking> getCustomerBookings(String customerId) {
        return bookingRepository.findByCustomerId(customerId);
    }

    public List<Booking> getProviderBookings(String providerId) {
        return bookingRepository.findByProviderId(providerId);
    }

    public Booking getBookingById(String id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
    }

    public Booking createBooking(BookingRequest request, Authentication authentication) {
        User customer = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Service service = serviceRepository.findById(request.getServiceId())
                .orElseThrow(() -> new RuntimeException("Service not found"));

        TimeSlot timeSlot = TimeSlot.builder()
                .id(request.getTimeSlotId())
                .startTime("09:00")
                .endTime("10:00")
                .isAvailable(true)
                .build();

        Booking booking = Booking.builder()
                .serviceId(service.getId())
                .service(service)
                .customerId(customer.getId())
                .customer(customer)
                .providerId(service.getProviderId())
                .provider(service.getProvider())
                .date(request.getDate())
                .timeSlot(timeSlot)
                .status(BookingStatus.PENDING)
                .totalAmount(service.getPrice())
                .notes(request.getNotes())
                .build();

        return bookingRepository.save(booking);
    }

    public Booking updateBookingStatus(String id, BookingStatus status) {
        Booking booking = getBookingById(id);
        booking.setStatus(status);
        return bookingRepository.save(booking);
    }

    public Booking cancelBooking(String id) {
        Booking booking = getBookingById(id);
        booking.setStatus(BookingStatus.CANCELLED);
        return bookingRepository.save(booking);
    }
}
