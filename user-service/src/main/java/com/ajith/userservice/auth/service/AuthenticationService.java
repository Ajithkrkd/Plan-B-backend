package com.ajith.userservice.auth.service;

import com.ajith.userservice.auth.dto.LoginRequest;
import com.ajith.userservice.auth.dto.LoginResponse;
import com.ajith.userservice.auth.dto.RegistrationRequest;
import com.ajith.userservice.model.Role;
import com.ajith.userservice.model.User;
import com.ajith.userservice.repository.UserRepository;
import com.ajith.userservice.utils.BasicResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public ResponseEntity< BasicResponse> register (RegistrationRequest request) {
        var user = User.builder ( )
                .fullName ( request.getFullName () )
                .phoneNumber ( request.getPhoneNumber () )
                .email ( request.getEmail () )
                .password (passwordEncoder.encode (request.getPassword ()))
                .joinDate ( new Date ( ))
                .role ( Role.USER )
                .build ();
        User savedWorker = userRepository.save ( user );
        return ResponseEntity.status( HttpStatus.CREATED)
                .body(BasicResponse.builder()
                        .message ("User Registered successfully")
                        .description ( "The user has been registered " )
                        .timestamp ( LocalDateTime.now () )
                        .status ( HttpStatus.CREATED.value ( ) )
                        .build());
    }

    public LoginResponse login (LoginRequest request) {

    }
}
