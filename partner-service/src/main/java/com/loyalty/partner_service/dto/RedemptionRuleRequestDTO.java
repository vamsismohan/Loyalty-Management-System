package com.loyalty.partner_service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RedemptionRuleRequestDTO {

    private String partner;
    private String pointType;
    private Long pointsRequired;
    private String rewardType;
    private Boolean active;
}
