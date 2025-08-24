package com.complete.api.gateway.exceptions;


public class SuccessNotification extends RuntimeException {
    private String message;

    public SuccessNotification(String message) {
        super(message);
        this.message = message;
    }

    public SuccessNotification() {

    }
}
