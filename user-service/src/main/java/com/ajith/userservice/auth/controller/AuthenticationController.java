package com.ajith.userservice.auth.controller;

import com.ajith.userservice.auth.dto.LoginRequest;
import com.ajith.userservice.auth.dto.LoginResponse;
import com.ajith.userservice.auth.dto.RegistrationRequest;
import com.ajith.userservice.auth.service.AuthenticationService;
import com.ajith.userservice.kafka.event.UserEmailTokenEvent;
import com.ajith.userservice.kafka.service.KafkaProducer;
import com.ajith.userservice.service.IUserService;
import com.ajith.userservice.utils.BasicResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final IUserService userService;
    private final AuthenticationService authenticationService;
    private final KafkaProducer kafkaProducer;
    @PostMapping("/register")
    public ResponseEntity<BasicResponse> registerUser(@RequestBody RegistrationRequest request)
    {
        try {
            boolean existEmail = userService.isEmailExist(request.getEmail());
            if (existEmail) {
                return ResponseEntity.status( HttpStatus.BAD_REQUEST)
                        .body(BasicResponse.builder()
                                .message ("Email already exists")
                                .timestamp ( LocalDateTime.now () )
                                .description ( "There is conflict with already existing email" )
                                .status ( HttpStatus.CONFLICT.value ( ) )
                                .build());
            }

            ResponseEntity<BasicResponse> response = authenticationService.register(request);
           UserEmailTokenEvent event = authenticationService.createUserEmailEvent(request);
            kafkaProducer.sentMessage (event);
            return response;
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(BasicResponse.builder()
                            .message (e.getMessage ())
                            .description ( "server side error" )
                            .timestamp ( LocalDateTime.now () )
                            .status ( HttpStatus.INTERNAL_SERVER_ERROR.value ( ) )
                            .build());
        }
    }




    @PostMapping("/login")
    public ResponseEntity < LoginResponse > login(@RequestBody LoginRequest request){
            LoginResponse response = authenticationService.login(request);
            return ResponseEntity.ok(response);
    }

}
