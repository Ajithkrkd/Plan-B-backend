package com.ajith.userservice.service;

import com.ajith.userservice.model.User;
import com.ajith.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class userService implements IUserService{

    private  final UserRepository userRepository;

    @Override
    public boolean isEmailExist (String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public Optional < User > findByEmail (String email) {
        return userRepository.findByEmail ( email );
    }
}
