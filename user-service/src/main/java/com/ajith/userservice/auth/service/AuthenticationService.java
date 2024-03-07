package com.ajith.userservice.auth.service;

import com.ajith.userservice.GlobalExceptionHandler.Exceptions.EmailNotVerifiedException;
import com.ajith.userservice.GlobalExceptionHandler.Exceptions.UserBlockedException;
import com.ajith.userservice.GlobalExceptionHandler.Exceptions.CustomBadcredentialException;
import com.ajith.userservice.GlobalExceptionHandler.Exceptions.UserNotFoundException;
import com.ajith.userservice.auth.dto.LoginRequest;
import com.ajith.userservice.auth.dto.LoginResponse;
import com.ajith.userservice.auth.dto.RegistrationRequest;
import com.ajith.userservice.config.JwtService;
import com.ajith.userservice.user.model.Role;
import com.ajith.userservice.user.model.User;
import com.ajith.userservice.user.repository.UserRepository;
import com.ajith.userservice.user.token.TokenService;
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
import java.util.Optional;

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
            throw new CustomBadcredentialException ("email or password is incorrect");
        } catch (UsernameNotFoundException e) {
            throw new UsernameNotFoundException("Worker not found");
        }
        catch (Exception e){
        throw new RuntimeException (e.getMessage ());
        }
    }

    public ResponseEntity< BasicResponse> confirmEmailWithToken (String token) {

        try {
            Optional<User> userWithToken = userRepository.findByVerificationToken ( token );
            if ( userWithToken.isPresent())
            {
                User user = userWithToken.get ();
                user.setEmailVerified ( true );
                userRepository.save ( user );

                return ResponseEntity.status ( HttpStatus.OK )
                        .body ( BasicResponse.builder ()
                                .message ( "Verification Success" )
                                .description ( "Verification success with token worker is confirmed" )
                                .status ( HttpStatus.OK.value ( ) )
                                .timestamp ( LocalDateTime.now () )
                                .build ()
                        );
            }
            else {
                throw new UserNotFoundException ("user with token " + token + " not found");
            }
        }
        catch ( Exception e)
        {
            throw new RuntimeException ( e.getMessage ());
        }


    }
}
