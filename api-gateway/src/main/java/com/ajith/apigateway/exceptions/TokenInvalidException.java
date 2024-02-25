package com.ajith.apigateway.exceptions;

public class TokenInvalidException extends RuntimeException{
    public TokenInvalidException (String message) {
        super(message);
    }
}
