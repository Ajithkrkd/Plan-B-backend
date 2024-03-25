package com.ajith.workItemservice.members.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    private long userId;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String  role;
    private boolean isEmailVerified;
    private String profile_image_path;
    private boolean isBlocked;
    private Date joinDate;
}

