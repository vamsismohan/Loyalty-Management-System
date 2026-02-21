package com.loyalty.partner_service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccrualRuleRequestDTO {

    private String partner;
    private String pointType;
    private Double pointsPerUnit;
    private String unitType;
    private Boolean active;
}
