package com.ajith.apigateway.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {AuthHeaderNotFountException.class})
    public ResponseEntity<ErrorMessage>authHeaderNotFount(AuthHeaderNotFountException ex)
    {
        return ResponseEntity.status ( HttpStatus.FORBIDDEN ).body (ErrorMessage.builder ()
                .message ( ex.getMessage ())
                .description ( "provide the authorization header to hit this end point" )
                .timestamp ( LocalDateTime.now () )
                .status ( HttpStatus.FORBIDDEN.value ( ) )
                .build ());
    }
    @ExceptionHandler(value = {TokenInvalidException.class})
    public ResponseEntity<ErrorMessage>tokenInvalid(TokenInvalidException ex)
    {
        return ResponseEntity.status ( HttpStatus.FORBIDDEN ).body (ErrorMessage.builder ()
                .message ( ex.getMessage ())
                .description ( "token expired please login to get new token" )
                .timestamp ( LocalDateTime.now () )
                .status ( HttpStatus.FORBIDDEN.value ( ) )
                .build ());
    }
}
