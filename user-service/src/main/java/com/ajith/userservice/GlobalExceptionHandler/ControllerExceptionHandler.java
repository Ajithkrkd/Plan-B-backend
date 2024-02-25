package com.ajith.userservice.GlobalExceptionHandler;

import com.ajith.userservice.GlobalExceptionHandler.Exceptions.EmailAlreadyExistsException;
import com.ajith.userservice.GlobalExceptionHandler.Exceptions.EmailNotVerifiedException;
import com.ajith.userservice.GlobalExceptionHandler.Exceptions.UserBlockedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler (value = {UserBlockedException.class})
    @ResponseStatus (value = HttpStatus.NOT_FOUND)
    public ErrorMessage UserBlockedException(UserBlockedException ex ,WebRequest request) {
        ErrorMessage message = new ErrorMessage();
        message.setStatus (HttpStatus.NOT_FOUND.value ());
        message.setMessage ( ex.getMessage() );
        message.setDescription ( "worker is blocked try to connect with support" );
        message.setTimestamp ( LocalDateTime.now ( ) );
        return message;
    }

    @ExceptionHandler (value = {EmailNotVerifiedException.class})
    @ResponseStatus (value = HttpStatus.UNAUTHORIZED)
    public ErrorMessage EmailVerificationException(EmailNotVerifiedException ex ,WebRequest request) {
        ErrorMessage message = new ErrorMessage();
        message.setStatus (HttpStatus.UNAUTHORIZED.value ());
        message.setMessage ( ex.getMessage() );
        message.setDescription ( " This email is not verified yet check your mail for verification link" );
        message.setTimestamp ( LocalDateTime.now ( ) );
        return message;
    }


    @ExceptionHandler(value = {EmailAlreadyExistsException.class})
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ErrorMessage emailAlreadyExist(EmailAlreadyExistsException ex,WebRequest request)
    {
        ErrorMessage message = new ErrorMessage();
        message.setStatus (HttpStatus.CONFLICT.value ());
        message.setMessage ( ex.getMessage() );
        message.setDescription ( "There is conflict between emails , this email already in use" );
        message.setTimestamp ( LocalDateTime.now ( ) );
        return message;
    }



}