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
public class AccrualRuleResponseDTO {

    private String ruleId;
    private String partner;
    private String pointType;
    private Double pointsPerUnit;
    private String unitType;
    private Boolean active;
}
