package com.ajith.userservice.user.service;

import com.ajith.userservice.user.dto.ChangePasswordRequest;
import com.ajith.userservice.user.dto.ForgotPasswordRequest;
import com.ajith.userservice.user.dto.UserDetailsResponse;
import com.ajith.userservice.user.dto.UserUpdateRequest;
import com.ajith.userservice.user.model.User;
import com.ajith.userservice.utils.BasicResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

public interface IUserService {
    boolean isEmailExist (String email);
    Optional< User > findByEmail (String email);
    ResponseEntity< BasicResponse > addProfileImageForUser (String authHeader, MultipartFile file) throws IOException;

    ResponseEntity< BasicResponse> changePassword (String authHeader, ChangePasswordRequest changePasswordRequest);

    ResponseEntity< BasicResponse> getForgotPasswordLink (String userEmail);

    ResponseEntity< UserDetailsResponse > getUserDetails (String authHeader);

    ResponseEntity< BasicResponse> updateUserDetails (UserUpdateRequest userUpdateRequest, String authHeader);

    ResponseEntity< BasicResponse> forgot_password (ForgotPasswordRequest forgotPasswordRequest);

    ResponseEntity < UserDetailsResponse > getUserByAuthHeader (String authHeader);
}
