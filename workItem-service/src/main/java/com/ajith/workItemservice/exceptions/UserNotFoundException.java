package com.ajith.workItemservice.exceptions;


public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException (String message) {
        super(message);
    }
}
