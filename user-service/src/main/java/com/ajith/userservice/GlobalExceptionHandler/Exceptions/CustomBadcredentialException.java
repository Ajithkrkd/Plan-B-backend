package com.ajith.userservice.GlobalExceptionHandler.Exceptions;

public class CustomBadcredentialException extends RuntimeException {
    public CustomBadcredentialException (String message) {
        super(message);
    }
}
