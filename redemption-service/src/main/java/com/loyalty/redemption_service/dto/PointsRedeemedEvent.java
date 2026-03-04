package com.loyalty.redemption_service.dto;

import java.time.Instant;

import lombok.Data;

@Data
public class PointsRedeemedEvent {
    private String membershipNumber;
    private String partner;
    private String pointType;
    private Long points;
    private String referenceId;
    private String rewardType;
    private Instant createdAt = Instant.now();
}
