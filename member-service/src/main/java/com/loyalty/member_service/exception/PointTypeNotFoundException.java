package com.loyalty.member_service.exception;

public class PointTypeNotFoundException extends RuntimeException {

    public PointTypeNotFoundException(String pointType) {
        super("Point Type Not Found: " + pointType);
    }
}
