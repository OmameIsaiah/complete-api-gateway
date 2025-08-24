package com.complete.api.gateway.exceptions;

public class SuccessResponse extends RuntimeException {
    private String message;

    public SuccessResponse(String message) {
        super(message);
        this.message = message;
    }
}
