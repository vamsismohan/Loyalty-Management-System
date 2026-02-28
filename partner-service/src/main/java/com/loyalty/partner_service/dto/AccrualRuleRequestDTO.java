package com.loyalty.partner_service.dto;

import io.github.resilience4j.core.lang.NonNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccrualRuleRequestDTO {

    @NonNull
    private String partner;
    @NonNull
    private String pointType;
    @NonNull
    private Double pointsPerUnit;
    @NonNull
    private String unitType;
    @NonNull
    private Boolean active;
}
