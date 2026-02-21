package com.loyalty.partner_service.exception;

public class PartnerNotFoundException extends RuntimeException {

    public PartnerNotFoundException(String message) {
        super("Partner not found: "+ message);
    }

}
