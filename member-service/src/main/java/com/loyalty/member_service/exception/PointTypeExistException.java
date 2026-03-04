package com.loyalty.member_service.exception;

public class PointTypeExistException extends RuntimeException {

    public PointTypeExistException(String message) {
        super("Point Type Already Exist: " + message);
    }
}
