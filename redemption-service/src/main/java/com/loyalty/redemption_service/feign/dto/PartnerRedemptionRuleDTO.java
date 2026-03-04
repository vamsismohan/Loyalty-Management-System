package com.loyalty.redemption_service.feign.dto;

import lombok.Data;

@Data
public class PartnerRedemptionRuleDTO {
    private String partner;
    private String pointType;
    private Long pointsRequired;
    private String rewardType;
    private Boolean active;
}
