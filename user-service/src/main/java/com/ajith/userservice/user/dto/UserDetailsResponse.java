package com.ajith.userservice.user.dto;

import com.ajith.userservice.user.model.Role;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDetailsResponse {
    private long userId;
    private String fullName;
    private String email;
    private String phoneNumber;
    private Role  role;
    private boolean isEmailVerified;
    private String profile_image_path;
    private boolean isBlocked;
}
