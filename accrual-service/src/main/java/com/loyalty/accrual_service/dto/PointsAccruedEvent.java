package com.loyalty.accrual_service.dto;

import java.time.Instant;

import lombok.Data;

@Data
public class PointsAccruedEvent {
    private String membershipNumber;
    private String partner;
    private String pointType;
    private Long points;
    private String referenceId;
    private String activityType;
    private Long activityValue;
    private String cabinClass;
    private Instant createdAt = Instant.now();
}
