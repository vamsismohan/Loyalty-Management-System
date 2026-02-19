package com.loyalty.customer_service.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class CustomerRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private LocalDate dob;
    private String country;
    private List<AddressRequest> addresses;

}
