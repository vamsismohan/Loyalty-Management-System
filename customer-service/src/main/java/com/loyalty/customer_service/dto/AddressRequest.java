package com.loyalty.customer_service.dto;

import lombok.Data;

@Data
public class AddressRequest {
    private String addressLine;
    private String city;
    private String country;
    private String postalCode;
}

