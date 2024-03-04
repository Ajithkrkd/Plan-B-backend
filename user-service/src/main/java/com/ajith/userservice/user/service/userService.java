package com.ajith.userservice.user.service;

import com.ajith.userservice.GlobalExceptionHandler.Exceptions.UserNotFoundException;
import com.ajith.userservice.user.dto.ChangePasswordRequest;
import com.ajith.userservice.user.dto.UserDetailsResponse;
import com.ajith.userservice.user.dto.UserUpdateRequest;
import com.ajith.userservice.user.model.User;
import com.ajith.userservice.user.repository.UserRepository;
import com.ajith.userservice.utils.BasicResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class userService implements IUserService{

    private  final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean isEmailExist (String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public Optional < User > findByEmail (String email) {
        return userRepository.findByEmail ( email );
    }

    @Override
    public ResponseEntity < BasicResponse > addProfileImageForUser
            (String userEmail, MultipartFile imageFile) throws IOException {
        try {

            Optional<User> optionalUser = userRepository.findByEmail ( userEmail );
            if( optionalUser.isPresent ( ) ){
                User validUser = optionalUser.get();
                String fileName = FileUploadService.uploadFile(imageFile);
                validUser.setProfileImage("/uploads"+"/"+fileName);
                userRepository.save(validUser);
                return ResponseEntity.ok(BasicResponse.builder ( )
                                .message ( "Successfully added profile image" )
                                .description ( "profile image uploaded to user "+ userEmail )
                                .timestamp ( LocalDateTime.now () )
                                .status ( HttpStatus.OK.value ( ) )
                        .build ( ) );
            }else{
                throw new UserNotFoundException ( "User " + userEmail + "not found");
            }

        }catch (UserNotFoundException e){
            throw new RuntimeException(e.getMessage ());
        }
    }

    @Override
    public ResponseEntity < BasicResponse > changePassword (String userEmail, ChangePasswordRequest changePasswordRequest) {
        try {
            Optional < User > optionalUser = userRepository.findByEmail ( userEmail );
            if ( optionalUser.isPresent ( ) ) {
                User validUser = optionalUser.get ( );

                if ( passwordEncoder.matches ( changePasswordRequest.getCurrentPassword () , validUser.getPassword ( ) ) ) {
                    validUser.setPassword ( passwordEncoder.encode ( changePasswordRequest.getNewPassword ( ) ) );
                    userRepository.save ( validUser );
                    return ResponseEntity.status ( HttpStatus.OK ).body ( BasicResponse
                            .builder ( )
                            .message ( "Your password is changed" )
                            .status ( HttpStatus.OK.value ( ) )
                            .timestamp ( LocalDateTime.now ( ) )
                            .description ( "password changed successfully " )
                            .build ( ) );
                } else {
                    return ResponseEntity.status ( HttpStatus.BAD_REQUEST ).body ( BasicResponse
                            .builder ( )
                            .message ( "Your current password entered is wrong check it" )
                            .status ( HttpStatus.BAD_REQUEST.value ( ) )
                            .timestamp ( LocalDateTime.now ( ) )
                            .description ( changePasswordRequest.getCurrentPassword ( ) + "  this password is wrong check try another" )
                            .build ( ) );
                }

            }else {
                throw new UserNotFoundException ( "User " + userEmail+ " does not exist" );
            }
        } catch (UserNotFoundException e) {
            throw new RuntimeException ( e.getMessage ( ) );
        }
    }

    @Override
    public ResponseEntity < BasicResponse > forgot_password (String userEmail) {
        return null;
    }

    @Override
    public ResponseEntity < UserDetailsResponse > getUserDetails (String userEmail) {

        try{
            Optional<User> optionalUser = userRepository.findByEmail ( userEmail );
            if( optionalUser.isPresent ( ) ){
                User validUser = optionalUser.get();
                return getUserDetailsResponseResponseEntity ( validUser );

            }else{
                throw new UserNotFoundException ( "User " + userEmail + "  does not exist"  );
            }
        }catch (UserNotFoundException e){
            throw new RuntimeException ( e.getMessage () );
        }catch (Exception e){
            throw new RuntimeException ( e.getMessage () );
        }

    }

    private static ResponseEntity < UserDetailsResponse > getUserDetailsResponseResponseEntity (User validUser) {
        return ResponseEntity.ok ( UserDetailsResponse.builder ( )
                .userId ( validUser.getId ( ) )
                .fullName ( validUser.getFullName ( ) )
                .email ( validUser.getEmail ( ) )
                .isBlocked ( validUser.isBlocked ( ) )
                .profile_image_path ( validUser.getProfileImage ( ) )
                .phoneNumber ( validUser.getPhoneNumber ( ) )
                .role ( validUser.getRole ( ) )
                .isEmailVerified ( validUser.isEmailVerified ( ) )
                .build ( ) );
    }

    @Override
    public ResponseEntity < BasicResponse > updateUserDetails (UserUpdateRequest userUpdateRequest, String userEmail) {
        try{
            Optional<User> optionalUser = userRepository.findByEmail ( userEmail );
            if( optionalUser.isPresent ( ) ){
                User validUser = optionalUser.get();
                return updateUserDetailsWithNewData ( validUser, userUpdateRequest );

            }else{
                throw new UserNotFoundException ( "User " + userEmail + "  does not exist"  );
            }
        }catch (UserNotFoundException e){
            throw new RuntimeException ( e.getMessage () );
        }catch (Exception e){
            throw new RuntimeException ( e.getMessage () );
        }
    }

    private ResponseEntity< BasicResponse> updateUserDetailsWithNewData (
            User validUser, UserUpdateRequest userUpdateRequest) {

        validUser.setFullName ( userUpdateRequest.getFullName () );
        validUser.setPhoneNumber ( userUpdateRequest.getPhoneNumber () );
        userRepository.save ( validUser );
        return ResponseEntity.ok ( BasicResponse.builder()
                        .description ( "User " + userUpdateRequest.getFullName() + " updated")
                        .message ( "user updated with new details" )
                        .timestamp ( LocalDateTime.now () )
                        .status ( HttpStatus.OK.value ( ) )
                .build() )  ;
    }
}
