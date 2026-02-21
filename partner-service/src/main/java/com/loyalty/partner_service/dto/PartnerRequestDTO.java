package com.loyalty.partner_service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartnerRequestDTO {

    private String partner;
    private String partnerType;
    private String status;
}
