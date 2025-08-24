package com.complete.api.gateway.exceptions;

public class TooManyRequestException extends RuntimeException {
    private String message;

    public TooManyRequestException(String message) {
        super(message);
        this.message = message;
    }
}
