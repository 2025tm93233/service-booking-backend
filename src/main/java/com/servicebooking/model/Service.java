package com.servicebooking.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "services")
public class Service {

    @Id
    private String id;

    private String title;
    private String description;
    private Category category;
    private String providerId;
    private User provider;
    private Double price;
    private PriceUnit priceUnit;
    private List<String> images;
    private Double rating;
    private Integer reviewCount;
    private String location;
    private List<String> availability;
    private List<String> tags;
    private Boolean isActive;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();
}
