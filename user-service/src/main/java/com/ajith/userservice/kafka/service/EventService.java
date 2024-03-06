package com.ajith.userservice.kafka.service;

import com.ajith.userservice.auth.dto.RegistrationRequest;
import com.ajith.userservice.kafka.event.ForgottenPasswordEvent;
import com.ajith.userservice.kafka.event.UserEmailTokenEvent;
import com.ajith.userservice.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventService {


    private final UserRepository userRepository;
    @Value ( "${user.email.verification.link}" )
    private String confirmation_link;
    @Value ( "${user.forgotten.password.link}" )
    private String forgottenPassword_link;

    public UserEmailTokenEvent createUserEmailEvent (RegistrationRequest request) {
        var token = getEmailVerificationToken(request.getEmail ());
        return UserEmailTokenEvent.builder ( )
                .email ( request.getEmail ( ) )
                .fullName ( request.getFullName ( ) )
                .token ( token )
                .confirmation_link ( confirmation_link )
                .build ( );
    }
    public ForgottenPasswordEvent createForgottenPasswordEvent (String email, String fullName) {
        var token = getEmailVerificationToken(email);
        return ForgottenPasswordEvent.builder ( )
                .email ( email )
                .fullName ( fullName )
                .token ( token )
                .confirmation_link ( forgottenPassword_link )
                .build ( );
    }
    private String getEmailVerificationToken (String email) {
        String token = createEmailVerificationToken (email);
        saveEmailVerificationTokenToUser (email,token);
        return token;
    }

    private String createEmailVerificationToken ( String email ) {
        return UUID.randomUUID ().toString ();
    }


    private void saveEmailVerificationTokenToUser (String email,String token) {
        var user = userRepository.findByEmail ( email )
                .orElseThrow (()->new UsernameNotFoundException ( "user not fount with email" + email ));
        user.setVerificationToken ( token );
        userRepository.save ( user );
    }

}
