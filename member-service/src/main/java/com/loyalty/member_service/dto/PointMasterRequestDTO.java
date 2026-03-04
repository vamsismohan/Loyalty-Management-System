package com.loyalty.member_service.dto;

import io.github.resilience4j.core.lang.NonNull;
import lombok.Data;

@Data
public class PointMasterRequestDTO {

    @NonNull
    private String pointType;

    @NonNull
    private Boolean isTierQualifying;

    @NonNull
    private Integer expiryMonths;

    @NonNull
    private Long maxLimit;
}
