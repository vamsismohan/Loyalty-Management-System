package com.loyalty.accrual_service.dto;

import lombok.Data;

@Data
public class AccrualRequestDTO {
    private String membershipNumber;
    private String partner;
    private String pointType;
    private String activityType; // FLIGHT / NON_FLIGHT
    private Long activityValue; // e.g., distance or amount
    private String cabinClass; // ECONOMY / PREMIUM / BUSINESS
    private String referenceId;
}
