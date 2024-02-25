package com.ajith.userservice.auth.service;

import com.ajith.userservice.GlobalExceptionHandler.Exceptions.EmailNotVerifiedException;
import com.ajith.userservice.GlobalExceptionHandler.Exceptions.UserBlockedException;
import com.ajith.userservice.auth.dto.LoginRequest;
import com.ajith.userservice.auth.dto.LoginResponse;
import com.ajith.userservice.auth.dto.RegistrationRequest;
import com.ajith.userservice.config.JwtService;
import com.ajith.userservice.kafka.event.UserEmailTokenEvent;
import com.ajith.userservice.model.Role;
import com.ajith.userservice.model.User;
import com.ajith.userservice.repository.UserRepository;
import com.ajith.userservice.token.TokenService;
import com.ajith.userservice.utils.BasicResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final TokenService tokenService;
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
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken (
                            request.getEmail(),
                            request.getPassword()
                    )
            );
            var user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new UsernameNotFoundException ("user not found"));

            if (user.isBlocked ()) {
                throw new UserBlockedException ("user is blocked");
            }
            if(!user.isEmailVerified ())
            {
                System.out.println ("Worker is not----------------------------------------" );
                throw new EmailNotVerifiedException ("email verification failed");
            }
            var jwtToken = jwtService.generateToken(user);
            var refreshToken = jwtService.generateRefreshToken ( user );

            tokenService.revokeAllTokens ( user );
            tokenService.saveUserToken ( user, refreshToken );


            return
                    LoginResponse.builder()
                            .access_token (jwtToken)
                            .refresh_token ( refreshToken )
                            .build();
        }
        catch (EmailNotVerifiedException e){
            throw new EmailNotVerifiedException ( e.getMessage () );
        }
        catch (BadCredentialsException e) {
            throw new BadCredentialsException ("Password is Wrong");
        } catch (UsernameNotFoundException e) {
            throw new UsernameNotFoundException("Worker not found");
        }
    }

    public UserEmailTokenEvent createUserEmailEvent (RegistrationRequest request) {
        var token = getEmailVerificationToken(request.getEmail ());
        return UserEmailTokenEvent.builder ( )
                .email ( request.getEmail ( ) )
                .fullName ( request.getFullName ( ) )
                .token ( token ).build ( );
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
        user.setEmailVerificationToken ( token );
        userRepository.save ( user );
    }
}
