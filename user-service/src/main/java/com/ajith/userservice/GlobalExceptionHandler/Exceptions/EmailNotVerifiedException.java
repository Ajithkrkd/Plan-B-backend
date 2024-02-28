package com.ajith.userservice.GlobalExceptionHandler.Exceptions;

public class EmailNotVerifiedException extends RuntimeException {
    public EmailNotVerifiedException (String message) {
        super(message);

    }
}
