package com.yourtravelcompanion.your_travel_companion.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class TripDto {
    private Long id;
    private String destination;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;
    private LocalDateTime created;
    private Long userId;
    private Long countryId;
}
