package com.loyalty.customer_service.exception;

public class CustomerAlreadyExistsException extends RuntimeException{

    public CustomerAlreadyExistsException(String message) {
        super("Customer already exists with this email: " + message);
    }

}
