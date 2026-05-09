package com.servicebooking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequest {

    @NotBlank(message = "Service ID is required")
    private String serviceId;

    @NotBlank(message = "Date is required")
    private String date;

    @NotBlank(message = "Time slot ID is required")
    private String timeSlotId;

    private String notes;
}
