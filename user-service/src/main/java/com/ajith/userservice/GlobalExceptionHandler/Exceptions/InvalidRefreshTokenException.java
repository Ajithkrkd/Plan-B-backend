package com.ajith.userservice.GlobalExceptionHandler.Exceptions;

public class InvalidRefreshTokenException extends Throwable {
    public InvalidRefreshTokenException (String message) {
        super(message);
    }
}
