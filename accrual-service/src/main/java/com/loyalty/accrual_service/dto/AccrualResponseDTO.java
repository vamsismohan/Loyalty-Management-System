package com.loyalty.accrual_service.dto;

import lombok.Data;

@Data
public class AccrualResponseDTO {
    private Long transactionId;
    private String membershipNumber;
    private String status;
    private Long pointsEarned;
}
