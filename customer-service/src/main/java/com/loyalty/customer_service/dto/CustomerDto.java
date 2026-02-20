package com.loyalty.customer_service.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CustomerDto {

    private String customerNumber;
    private String email;
    private String status;
}
