package com.loyalty.partner_service.exception;

public class PointTypeNotExistException extends RuntimeException {

    public PointTypeNotExistException(String message){
        super("Given point type not exist in system: " + message);
    }
}
