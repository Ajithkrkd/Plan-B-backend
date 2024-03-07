package com.ajith.projectservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler (value = {UserNotFoundException.class})
    @ResponseStatus (value = HttpStatus.BAD_REQUEST)
    public ErrorMessage UserNotFountException(UserNotFoundException ex , WebRequest request) {
        ErrorMessage message = new ErrorMessage();
        message.setStatus (HttpStatus.BAD_REQUEST.value ());
        message.setMessage ( ex.getMessage() );
        message.setDescription ( " unable to find the user check end point request others" );
        message.setTimestamp ( LocalDateTime.now ( ) );
        return message;
    }
}
