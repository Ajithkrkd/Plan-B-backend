package com.ajith.userservice.service;

import com.ajith.userservice.auth.dto.LoginRequest;
import com.ajith.userservice.auth.dto.LoginResponse;

public interface IuserService {
    boolean isEmailExist (String email);
}
