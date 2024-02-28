package com.ajith.userservice.GlobalExceptionHandler.Exceptions;

public class InvalidAuthorizationHeaderException extends Throwable {
    public InvalidAuthorizationHeaderException (String message) {
    super(message);
    }
}
