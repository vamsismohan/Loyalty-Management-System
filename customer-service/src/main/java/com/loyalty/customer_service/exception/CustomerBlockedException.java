package com.loyalty.customer_service.exception;

public class CustomerBlockedException extends RuntimeException {

    public CustomerBlockedException(String error) {
        super(error);
    }

}
