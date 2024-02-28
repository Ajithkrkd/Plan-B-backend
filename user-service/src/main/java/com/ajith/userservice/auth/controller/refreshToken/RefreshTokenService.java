package com.ajith.userservice.auth.controller.refreshToken;

import com.ajith.userservice.GlobalExceptionHandler.Exceptions.InvalidAuthorizationHeaderException;
import com.ajith.userservice.GlobalExceptionHandler.Exceptions.InvalidRefreshTokenException;
import com.ajith.userservice.GlobalExceptionHandler.Exceptions.MissingUserInfoException;
import com.ajith.userservice.GlobalExceptionHandler.Exceptions.UserNotFoundException;
import com.ajith.userservice.auth.dto.LoginResponse;
import com.ajith.userservice.config.JwtService;
import com.ajith.userservice.user.model.User;
import com.ajith.userservice.user.repository.UserRepository;
import com.ajith.userservice.user.token.TokenService;
import com.ajith.userservice.utils.BasicResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final TokenService tokenService;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    public LoginResponse refreshToken(HttpServletRequest request, HttpServletResponse response) {
        try {

            final String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new InvalidAuthorizationHeaderException ("Invalid authorization header format");
            }

            // Extract refresh token and email from the header
            final String refreshToken = authHeader.substring(7);
            final String userEmail = jwtService.extractUsername(refreshToken);

            // Check if email is present and fetch user details
            if (userEmail != null) {
                Optional<User> optionalUser = userRepository.findByEmail(userEmail);
                if (!optionalUser.isPresent()) {
                    throw new UserNotFoundException("User " + userEmail + " not found");
                }

                // Validate refresh token from database and expiry
                User validUser = optionalUser.get();
                boolean isTokenValidFromUserDatabase = tokenService.checkRefreshTokenValidOrNot(refreshToken);
                boolean isRefreshTokenExpired = jwtService.isTokenValid(refreshToken, validUser);

                if (isTokenValidFromUserDatabase && isRefreshTokenExpired) {
                    return getNewLoginResponseWithTokens(validUser);
                } else {
                    throw new InvalidRefreshTokenException ("Refresh token is invalid or expired");
                }
            } else {
                throw new MissingUserInfoException ("Email information not found in the token");
            }
        } catch (InvalidAuthorizationHeaderException | InvalidRefreshTokenException | UserNotFoundException e) {
            // Handle specific authentication-related exceptions with appropriate messages
            return LoginResponse.builder()
                    .message( BasicResponse.builder().message ( e.getMessage() ).build())
                    .build();
        }
        catch (MissingUserInfoException e) {
            return LoginResponse.builder()
                    .message( BasicResponse.builder().message ( e.getMessage() ).build())
                    .build();
        }
        catch (Exception e) {
            return LoginResponse.builder()
                    .message(BasicResponse.builder().message ( e.getMessage() ).build())
                    .build();
        }
    }

    private LoginResponse getNewLoginResponseWithTokens (User validUser) {
        tokenService.revokeAllTokens ( validUser );
        String newRefreshToken = jwtService.generateRefreshToken ( validUser );
        String accessToken = jwtService.generateToken ( validUser);
        tokenService.saveUserToken ( validUser,newRefreshToken );
        return LoginResponse.builder ( )
                .refresh_token ( newRefreshToken )
                .access_token ( accessToken )
                .message ( getSuccessMessage() )
                .build ( );
    }

    private BasicResponse getSuccessMessage ( ) {
        return BasicResponse
                .builder()
                .message ( "Refresh token request is success" )
                .status ( HttpStatus.OK.value ( ) )
                .description ( "You got new refresh and access token " )
                .timestamp ( LocalDateTime.now () )
                .build();
    }

    private BasicResponse getErrorMessage ( ) {
        return BasicResponse
                .builder()
                        .message ( "Authorization failed" )
                        .status ( HttpStatus.BAD_REQUEST.value ( ) )
                        .description ( "Authorization failed auth header is missing or token invalid" )
                        .timestamp ( LocalDateTime.now () )
                        .build();
    }
}
