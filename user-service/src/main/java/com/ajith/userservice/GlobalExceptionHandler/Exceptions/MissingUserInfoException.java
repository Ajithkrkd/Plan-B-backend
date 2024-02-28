package com.ajith.userservice.GlobalExceptionHandler.Exceptions;

public class MissingUserInfoException extends Throwable {
    public MissingUserInfoException (String message) {
        super(message);
    }
}
