package com.loyalty.redemption_service.dto;

import lombok.Data;

@Data
public class RedemptionResponseDTO {
    private Long transactionId;
    private String membershipNumber;
    private String status;
    private Long pointsUsed;
}
