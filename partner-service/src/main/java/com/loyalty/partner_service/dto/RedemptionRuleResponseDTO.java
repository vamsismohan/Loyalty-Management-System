package com.loyalty.partner_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RedemptionRuleResponseDTO {

    private String ruleId;
    private String partner;
    private String pointType;
    private Long pointsRequired;
    private String rewardType;
    private Boolean active;
}
