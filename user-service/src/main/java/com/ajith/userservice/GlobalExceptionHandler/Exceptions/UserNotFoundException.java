package com.ajith.userservice.GlobalExceptionHandler.Exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException (String message) {
        super(message);
    }
}
