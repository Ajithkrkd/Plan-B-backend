package com.ajith.userservice.user.controllers;

import com.ajith.userservice.user.dto.ChangePasswordRequest;
import com.ajith.userservice.user.dto.UserDetailsResponse;
import com.ajith.userservice.user.dto.UserUpdateRequest;
import com.ajith.userservice.user.service.IUserService;
import com.ajith.userservice.utils.BasicResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/user/api/secure/")
@RequiredArgsConstructor
public class UserController {

    private final IUserService iUserService;
    @PostMapping("/{userEmail}/add_profile_image")
    public ResponseEntity< BasicResponse > addProfileImageForUser(
            @PathVariable("userEmail") String userEmail,
            @RequestParam("file") MultipartFile file) throws IOException {
        return  iUserService.addProfileImageForUser(userEmail ,file);
    }

    @PostMapping("/{userEmail}/update_user_details")
    public ResponseEntity< BasicResponse > updateUserDetails(
            @PathVariable String userEmail,
            @RequestBody UserUpdateRequest userUpdateRequest)
    {
        return iUserService.updateUserDetails(userUpdateRequest,userEmail);
    }

    @GetMapping("/{userEmail}/get_user_details")
    public ResponseEntity< UserDetailsResponse > getUserDetails(@PathVariable String userEmail)
    {
        System.out.println ("from here " );
        return iUserService.getUserDetails(userEmail);
    }

    @PostMapping("/{userEmail}/change_password")
    public ResponseEntity<BasicResponse>changePassword(
            @PathVariable String userEmail,
            @RequestBody ChangePasswordRequest changePasswordRequest){
        return iUserService.changePassword(userEmail,changePasswordRequest);
    }

    //TODO: service not implemented
    @PostMapping("/{userEmail}/forgot_password")
    public ResponseEntity<BasicResponse>forgot_password(
            @PathVariable String userEmail){
        return iUserService.forgot_password(userEmail);
    }
}
