package com.complete.api.gateway.exceptions;

public class InternalServerException extends RuntimeException {
    private String message;

    public InternalServerException(String message) {
        super(message);
        this.message = message;
    }
}
