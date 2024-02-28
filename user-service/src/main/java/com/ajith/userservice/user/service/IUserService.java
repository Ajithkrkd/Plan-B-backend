package com.ajith.userservice.user.service;

import com.ajith.userservice.user.model.User;

import java.util.Optional;

public interface IUserService {
    boolean isEmailExist (String email);
    Optional< User > findByEmail (String email);
}
