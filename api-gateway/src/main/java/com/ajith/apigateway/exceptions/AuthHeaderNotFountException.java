package com.ajith.apigateway.exceptions;

public class AuthHeaderNotFountException extends RuntimeException {
    public AuthHeaderNotFountException (String message) {
        super(message);
    }
}
