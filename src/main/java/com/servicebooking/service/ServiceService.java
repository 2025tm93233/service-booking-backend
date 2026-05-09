package com.servicebooking.service;

import com.servicebooking.model.Service;
import com.servicebooking.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ServiceService {

    @Autowired
    ServiceRepository serviceRepository;

    public List<Service> getAllServices() {
        return serviceRepository.findByIsActiveTrue();
    }

    public Page<Service> getServices(Pageable pageable) {
        return serviceRepository.findByIsActiveTrue(pageable);
    }

    public Service getServiceById(String id) {
        return serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found"));
    }

    public List<Service> getServicesByProvider(String providerId) {
        return serviceRepository.findByProviderId(providerId);
    }

    public Service createService(Service service) {
        service.setIsActive(true);
        return serviceRepository.save(service);
    }

    public Service updateService(String id, Service service) {
        Service existingService = getServiceById(id);
        existingService.setTitle(service.getTitle());
        existingService.setDescription(service.getDescription());
        existingService.setPrice(service.getPrice());
        existingService.setPriceUnit(service.getPriceUnit());
        existingService.setLocation(service.getLocation());
        existingService.setAvailability(service.getAvailability());
        existingService.setTags(service.getTags());
        existingService.setImages(service.getImages());
        return serviceRepository.save(existingService);
    }

    public void deleteService(String id) {
        Service service = getServiceById(id);
        service.setIsActive(false);
        serviceRepository.save(service);
    }
}
