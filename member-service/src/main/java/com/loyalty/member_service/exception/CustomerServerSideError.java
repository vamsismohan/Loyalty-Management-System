package com.loyalty.member_service.exception;

public class CustomerServerSideError extends RuntimeException {

    public CustomerServerSideError(String error){
        super(error);
    }

}
