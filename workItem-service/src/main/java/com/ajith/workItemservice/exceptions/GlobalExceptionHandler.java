package com.ajith.workItemservice.exceptions;

import com.ajith.workItemservice.utils.BasicResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler (value =ResourceNotFountException.class)
    @ResponseStatus (value = HttpStatus.NOT_FOUND)
    public BasicResponse resourceNotFoundException(ResourceNotFountException ex , WebRequest request) {
        BasicResponse message = new BasicResponse();
        message.setStatus (HttpStatus.NOT_FOUND.value ());
        message.setMessage ( ex.getMessage() );
        message.setDescription ( "The resource does not exist try another" );
        message.setTimestamp ( LocalDateTime.now ( ) );
        return message;
    }
    @ExceptionHandler (value = {UserNotFoundException.class})
    @ResponseStatus (value = HttpStatus.BAD_REQUEST)
    public BasicResponse UserNotFountException(UserNotFoundException ex , WebRequest request) {
        BasicResponse message = new BasicResponse();
        message.setStatus (HttpStatus.BAD_REQUEST.value ());
        message.setMessage ( ex.getMessage() );
        message.setDescription ( " unable to find the user check end point request others" );
        message.setTimestamp ( LocalDateTime.now ( ) );
        return message;
    }
    @ExceptionHandler (value = {ResourceDuplicationException.class})
    @ResponseStatus (value = HttpStatus.BAD_REQUEST)
    public BasicResponse ResourceDuplicationException(ResourceDuplicationException ex , WebRequest request) {
        BasicResponse message = new BasicResponse();
        message.setStatus (HttpStatus.BAD_REQUEST.value ());
        message.setMessage ( ex.getMessage() );
        message.setDescription ( "Your trying to create a resource is already exist try another" );
        message.setTimestamp ( LocalDateTime.now ( ) );
        return message;
    }

}
