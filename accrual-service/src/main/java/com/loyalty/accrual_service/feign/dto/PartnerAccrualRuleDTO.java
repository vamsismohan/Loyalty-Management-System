package com.loyalty.accrual_service.feign.dto;

import lombok.Data;

@Data
public class PartnerAccrualRuleDTO {
    private String partner;
    private String pointType;
    private Double pointsPerUnit;
    private String unitType; // DISTANCE / AMOUNT
    private Boolean active;
}
