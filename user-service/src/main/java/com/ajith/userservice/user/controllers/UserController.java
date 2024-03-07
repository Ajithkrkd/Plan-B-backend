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
@RequestMapping("/user/api/secure")
@RequiredArgsConstructor
public class UserController {

    private final IUserService iUserService;
    @PostMapping("/add_profile_image")
    public ResponseEntity< BasicResponse > addProfileImageForUser(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam("file") MultipartFile file) throws IOException {
        return  iUserService.addProfileImageForUser(authHeader ,file);
    }

    @PostMapping("/update_user_details")
    public ResponseEntity< BasicResponse > updateUserDetails(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody UserUpdateRequest userUpdateRequest)
    {
        return iUserService.updateUserDetails(userUpdateRequest,authHeader);
    }

    @GetMapping("/get_user_details")
    public ResponseEntity< UserDetailsResponse > getUserDetails(
            @RequestHeader("Authorization") String authHeader)
    {
        return iUserService.getUserDetails(authHeader);
    }

    @PostMapping("/change_password")
    public ResponseEntity<BasicResponse>changePassword(
            @RequestHeader  ("Authorization") String authHeader,
            @RequestBody ChangePasswordRequest changePasswordRequest){
        return iUserService.changePassword(authHeader,changePasswordRequest);
    }




}
