package com.ajith.userservice.service;

import com.ajith.userservice.auth.dto.LoginRequest;
import com.ajith.userservice.auth.dto.LoginResponse;
import com.ajith.userservice.model.User;

import java.util.Optional;

public interface IUserService {
    boolean isEmailExist (String email);
    Optional< User > findByEmail (String email);
}
