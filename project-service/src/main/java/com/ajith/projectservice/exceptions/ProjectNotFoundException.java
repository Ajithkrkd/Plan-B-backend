package com.ajith.projectservice.exceptions;

public class ProjectNotFoundException extends RuntimeException {
    public ProjectNotFoundException (String message) {
        super(message);
    }
}
