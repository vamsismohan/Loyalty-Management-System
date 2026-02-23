package com.loyalty.partner_service.exception;

public class PartnerAlreadyExistException extends RuntimeException {

    public PartnerAlreadyExistException(String message) {
        super("Partner already found: "+ message);
    }

}
