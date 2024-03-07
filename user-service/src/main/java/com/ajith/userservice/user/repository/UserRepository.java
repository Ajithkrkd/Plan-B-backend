package com.ajith.userservice.user.repository;

import com.ajith.userservice.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository< User ,Long > {

    Optional< User> findByEmail (String username);

    boolean existsByEmail (String email);

    Optional< User> findByVerificationToken (String token);
}
