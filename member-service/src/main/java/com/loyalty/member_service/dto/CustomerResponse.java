package com.loyalty.member_service.dto;

import java.util.List;

import lombok.Data;

@Data
public class CustomerResponse {
    private String customerNumber;
    private String firstName;
    private String lastName;
    private String email;
    private String status;
    private List<AddressRequest> addresses;

}

