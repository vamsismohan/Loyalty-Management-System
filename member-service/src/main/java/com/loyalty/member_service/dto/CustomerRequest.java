package com.loyalty.member_service.dto;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CustomerRequest {

    private String firstName;
    private String lastName;

    @Email
    @NonNull
    private String email;
    private String phone;
    private LocalDate dob;
    private String country;
    private List<AddressRequest> addresses;
}
