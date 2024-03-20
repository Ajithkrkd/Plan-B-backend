package com.ajith.userservice.user.service;

import com.ajith.userservice.GlobalExceptionHandler.Exceptions.ForgotPasswordTokenInvalidException;
import com.ajith.userservice.GlobalExceptionHandler.Exceptions.UserBlockedException;
import com.ajith.userservice.GlobalExceptionHandler.Exceptions.UserNotFoundException;
import com.ajith.userservice.config.JwtService;
import com.ajith.userservice.kafka.event.ForgottenPasswordEvent;
import com.ajith.userservice.kafka.service.EventService;
import com.ajith.userservice.kafka.service.KafkaProducer;
import com.ajith.userservice.user.dto.ChangePasswordRequest;
import com.ajith.userservice.user.dto.ForgotPasswordRequest;
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

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService{

    private  final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private  final FileUploadService fileUploadService;
    private final JwtService jwtService;
    private final EventService eventService;
    private final KafkaProducer kafkaProducer;

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
            (String authHeader, MultipartFile imageFile) throws IOException {
        try {

            Optional<User> optionalUser = jwtService.findUserWithAuthHeader ( authHeader );
            if( optionalUser.isPresent ( ) ){
                User validUser = optionalUser.get();
                String fileName = fileUploadService.uploadImageAndSaveImagePathToUser (imageFile);
                validUser.setProfileImage("/uploads"+"/"+fileName);
                userRepository.save(validUser);
                return ResponseEntity.ok(BasicResponse.builder ( )
                                .message ( "Successfully added profile image" )
                                .description ( "profile image uploaded to user " )
                                .timestamp ( LocalDateTime.now () )
                                .status ( HttpStatus.OK.value ( ) )
                        .build ( ) );
            }else{
                throw new UserNotFoundException ( "User doest not found");
            }

        }catch (UserNotFoundException e){
            throw new RuntimeException(e.getMessage ());
        }
    }

    @Override
    public ResponseEntity < BasicResponse > changePassword (String authHeader, ChangePasswordRequest changePasswordRequest) {
        try {
            System.out.println (changePasswordRequest.toString () );

            Optional < User > optionalUser = jwtService.findUserWithAuthHeader ( authHeader );

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
                throw new UserNotFoundException ( "User does not exist" );
            }
        } catch (UserNotFoundException e) {
            throw new RuntimeException ( e.getMessage ( ) );
        }
    }

    @Override
    public ResponseEntity < BasicResponse > getForgotPasswordLink (String userEmail) {
        try{
            Optional<User> optionalUser = userRepository.findByEmail ( userEmail );
            if( optionalUser.isPresent ( ) ){
                User validUser = optionalUser.get();
                ForgottenPasswordEvent event = eventService.createForgottenPasswordEvent ( validUser.getEmail ( ), validUser.getFullName ( ) );
                kafkaProducer.publishForgottenPassword ( event );
            return ResponseEntity.ok( BasicResponse.builder()
                            .message ( "Forgotten password email sent successfully" )
                            .description ( "check your email to view the forgot password link" )
                            .timestamp ( LocalDateTime.now () )
                            .status ( HttpStatus.OK.value ( ) )
                    .build());
            }else{
                throw new UserNotFoundException ( "User doest not exist with your token " );
            }
        }catch (UserNotFoundException e){
            throw new RuntimeException ( e.getMessage () );
        }catch (Exception e){
            throw new RuntimeException ( e.getMessage () );
        }
    }

    @Override
    public ResponseEntity < UserDetailsResponse > getUserDetails (String authHeader) {

        try{
            Optional<User> optionalUser = jwtService.findUserWithAuthHeader ( authHeader );
            if( optionalUser.isPresent ( ) ){
                User validUser = optionalUser.get();
                return getUserDetailsResponseResponseEntity ( validUser );

            }else{
                throw new UserNotFoundException ( "User doest not exist with your token " );
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
                .joinDate ( validUser.getJoinDate ( ) )
                .isEmailVerified ( validUser.isEmailVerified ( ) )
                .build ( ) );
    }

    @Override
    public ResponseEntity < BasicResponse > updateUserDetails (UserUpdateRequest userUpdateRequest, String authHeader) {
        try{
            Optional<User> optionalUser = jwtService.findUserWithAuthHeader ( authHeader );
            if( optionalUser.isPresent ( ) ){
                User validUser = optionalUser.get();
                return updateUserDetailsWithNewData ( validUser, userUpdateRequest );

            }else{
                throw new UserNotFoundException ( "User  does not exist"  );
            }
        }catch (UserNotFoundException e){
            throw new RuntimeException ( e.getMessage () );
        }catch (Exception e){
            throw new RuntimeException ( e.getMessage () );
        }
    }

    @Override
    public ResponseEntity < BasicResponse > forgot_password (ForgotPasswordRequest forgotPasswordRequest) {
        try{
            Optional<User> optionalUser = userRepository.findByVerificationToken ( forgotPasswordRequest.getToken () );
            if( optionalUser.isPresent ( ) ){
                User validUser = optionalUser.get();
                if(validUser.getVerificationToken ().equals ( forgotPasswordRequest.getToken () )){
                return updatePasswordWithNewOne(forgotPasswordRequest,validUser);
                }
                else{
                    throw new ForgotPasswordTokenInvalidException ("Your must provide valid token for forgotten password");
                }

            }else{
                throw new UserNotFoundException ( "User  does not exist with this forgot password token"  );
            }
        }catch (UserNotFoundException e){
            throw new RuntimeException ( e.getMessage () );
        }
        catch (ForgotPasswordTokenInvalidException e) {
            throw new RuntimeException ( e );
        }
        catch (Exception e){
            throw new RuntimeException ( e.getMessage () );
        }
    }

    @Override
    public ResponseEntity < UserDetailsResponse > getUserByAuthHeader (String authHeader) {
        try{
            Optional<User> optionalUser = jwtService.findUserWithAuthHeader ( authHeader );
            if( optionalUser.isPresent ( ) ){
                User validUser = optionalUser.get();
                return  getUserDetailsResponseResponseEntity ( validUser );

            }else{
                throw new UserNotFoundException ( "User  does not exist"  );
            }
        }catch (UserNotFoundException e){
            throw new RuntimeException ( e.getMessage () );
        }catch (Exception e){
            throw new RuntimeException ( e.getMessage () );
        }
    }

    @Override
    public ResponseEntity < List < UserDetailsResponse > > getUserByIds (String authHeader, List < Long > usersIds) {
        try{
            Optional<User> projectRootAdmin = jwtService.findUserWithAuthHeader ( authHeader );
            if ( !projectRootAdmin.isPresent()){
                throw new UserNotFoundException ( "project root Admin is not exist" );
            }
            if (projectRootAdmin.get ( ).isBlocked ( )){
                throw new UserBlockedException ("project root Admin is blocked");
            }
            List<User>usersList = userRepository.findAllById (usersIds);
            List<Long> missingUserIds = usersIds.stream()
                    .filter(id -> usersList.stream().noneMatch(user -> user.getId() == (id)))
                    .collect( Collectors.toList());

            if (!missingUserIds.isEmpty()) {
                String message = "Users with IDs " + missingUserIds + " not found.";
                throw new UserNotFoundException(message); // Example exception
            }
            List<UserDetailsResponse> userDetailsList = usersList.stream ()
                    .map (user -> convertUserToUserDetailsResponse(user))
                    .collect (Collectors.toList());
            return ResponseEntity.ok ( userDetailsList );
        } catch (UserNotFoundException e) {
            throw e;
        } catch (UserBlockedException e) {
            throw  e ;
        }catch (Exception e){
            throw new RuntimeException ( e.getMessage () );
        }
    }

    private UserDetailsResponse convertUserToUserDetailsResponse (User user) {
      return   UserDetailsResponse.builder ()
        .userId ( user.getId ( ) )
                .fullName ( user.getFullName ( ) )
                .email ( user.getEmail ( ) )
                .isBlocked ( user.isBlocked ( ) )
                .profile_image_path ( user.getProfileImage ( ) )
                .phoneNumber ( user.getPhoneNumber ( ) )
                .role ( user.getRole ( ) )
                .joinDate ( user.getJoinDate ( ) )
                .isEmailVerified ( user.isEmailVerified ( ) )
                .build ( ) ;
    }

    private ResponseEntity< BasicResponse> updatePasswordWithNewOne (ForgotPasswordRequest forgotPasswordRequest, User validUser) {
        validUser.setPassword ( passwordEncoder.encode ( forgotPasswordRequest.getNewPassword () ) );
        userRepository.save ( validUser );
        return ResponseEntity.ok ( BasicResponse.builder()
                .description ( "User " + validUser.getFullName() + " password updated")
                .message ( "Password updated successfully" )
                .timestamp ( LocalDateTime.now () )
                .status ( HttpStatus.OK.value ( ) )
                .build() )  ;
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
