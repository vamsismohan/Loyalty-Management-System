package com.loyalty.member_service.exception;

public class MemberAlreadyExistsException extends RuntimeException {

    public MemberAlreadyExistsException(String message) {
        super("Member Already Exists: "+ message);
    }
}
