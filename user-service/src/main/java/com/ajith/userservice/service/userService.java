package com.ajith.userservice.service;

import com.ajith.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class userService implements IuserService{

    private  final UserRepository userRepository;

    @Override
    public boolean isEmailExist (String email) {
        return userRepository.existsByEmail(email);
    }
}
