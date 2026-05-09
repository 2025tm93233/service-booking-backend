package com.servicebooking.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "timeSlots")
public class TimeSlot {

    @Id
    private String id;

    private String startTime;
    private String endTime;
    private Boolean isAvailable;
}
