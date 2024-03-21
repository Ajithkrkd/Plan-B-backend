package com.ajith.workItemservice.exceptions;

public class ResourceDuplicationException extends RuntimeException {
    public ResourceDuplicationException (String message) {
        super(message);
    }
}
