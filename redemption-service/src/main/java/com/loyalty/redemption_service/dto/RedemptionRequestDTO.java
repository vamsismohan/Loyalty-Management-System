package com.loyalty.redemption_service.dto;

import lombok.Data;

@Data
public class RedemptionRequestDTO {
    private String membershipNumber;
    private String partner;
    private String pointType;
    private Long pointsRequested;
    private String rewardType;
    private String referenceId;
}
