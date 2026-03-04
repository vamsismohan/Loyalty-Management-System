package com.loyalty.member_service.dto;

import lombok.Data;

@Data
public class PointMasterResponseDTO {

    private String pointType;

    private Boolean isTierQualifying;

    private Integer expiryMonths;

    private Long maxLimit;
}
