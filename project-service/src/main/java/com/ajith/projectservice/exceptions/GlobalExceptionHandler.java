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

    @ExceptionHandler (value =ResourceAlreadyExist.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ErrorMessage ResourceAlreadyExistsException(ResourceAlreadyExist ex , WebRequest request) {
        ErrorMessage message = new ErrorMessage();
        message.setStatus (HttpStatus.CONFLICT.value ());
        message.setMessage ( ex.getMessage() );
        message.setDescription ( "The resource already exist try another" );
        message.setTimestamp ( LocalDateTime.now ( ) );
        return message;
    }
    @ExceptionHandler (value =ResourceNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorMessage resourceNotFoundException(ResourceNotFoundException ex , WebRequest request) {
        ErrorMessage message = new ErrorMessage();
        message.setStatus (HttpStatus.NOT_FOUND.value ());
        message.setMessage ( ex.getMessage() );
        message.setDescription ( "The resource does not exist try another" );
        message.setTimestamp ( LocalDateTime.now ( ) );
        return message;
    }


    @ExceptionHandler (value = {ProjectNotFoundException.class})
    @ResponseStatus (value = HttpStatus.BAD_REQUEST)
    public ErrorMessage projectNotFoundException(ProjectNotFoundException ex , WebRequest request) {
        ErrorMessage message = new ErrorMessage();
        message.setStatus (HttpStatus.BAD_REQUEST.value ());
        message.setMessage ( ex.getMessage() );
        message.setDescription ( " unable to find the project check end point request others" );
        message.setTimestamp ( LocalDateTime.now ( ) );
        return message;
    }
}
