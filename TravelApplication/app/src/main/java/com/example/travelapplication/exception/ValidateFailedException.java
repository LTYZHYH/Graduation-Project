package com.example.travelapplication.exception;

public class ValidateFailedException extends RuntimeException {
    private String message;

    public ValidateFailedException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
