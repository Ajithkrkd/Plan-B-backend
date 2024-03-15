package com.ajith.userservice.auth.service;

import com.ajith.userservice.auth.dto.RegistrationRequest;
import com.ajith.userservice.config.JwtService;
import com.ajith.userservice.user.model.Role;
import com.ajith.userservice.user.model.User;
import com.ajith.userservice.user.repository.UserRepository;
import com.ajith.userservice.user.token.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class AuthenticationServiceTest {

    // service want to test
private AuthenticationService authenticationService;

// dependencies for authentication service
    private UserRepository mockUserRepository;
    private PasswordEncoder mockPasswordEncoder;
    private  AuthenticationManager mockAuthenticationManager;
    private  JwtService mockJwtService;
    private  TokenService mockTokenService;
    // ... other mock dependencies
    @BeforeEach
    public void setup() {
        mockUserRepository = mock(UserRepository.class);
        mockPasswordEncoder = mock(PasswordEncoder.class);
        mockJwtService = mock(JwtService.class);
        mockAuthenticationManager = mock( AuthenticationManager.class);
        mockTokenService = mock(TokenService.class);

         authenticationService = new AuthenticationService (
                mockUserRepository,
                mockPasswordEncoder,
                mockAuthenticationManager,
                mockJwtService,
                mockTokenService
                );
    }

    @Test
    public void registrationRequestToUser(){

        RegistrationRequest registrationRequest = new RegistrationRequest (
                "username","9048543203","ajith2255iti@gmail.com","password"
        );
        //Act
        User user = authenticationService.registrationRequestToUser ( registrationRequest );

        //Assertion

        assertTrue ( user.getRole () == Role.USER );
        assertTrue ( user.getEmail () == registrationRequest.getEmail () );
        assertTrue ( user.getFullName () == registrationRequest.getFullName () );
        assertTrue ( user.getPhoneNumber () == registrationRequest.getPhoneNumber () );
        assertTrue ( user.getPassword () == mockPasswordEncoder.encode ( registrationRequest.getPassword ()) );

    }
    @Test
    public void should_throw_nullPointerException_when_userRegistrationRequest_is_null(){
       var exception = assertThrows ( NullPointerException.class ,()->authenticationService.registrationRequestToUser ( null )  );
        assertEquals ( "registration request is null" ,exception.getMessage () );
    }
}