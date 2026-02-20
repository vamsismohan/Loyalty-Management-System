package com.loyalty.member_service.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class MemberResponse {

    private String membershipNumber;
    private String customerNumber;
    private String tierLevel;
    private String status;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private LocalDate dob;
    private String country;
    private List<AddressRequest> addresses;
}
