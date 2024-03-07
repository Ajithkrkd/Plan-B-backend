package com.ajith.projectservice.exceptions;

public class UserNotFoundException extends Throwable {
    public UserNotFoundException (String message) {
        super(message);
    }
}
