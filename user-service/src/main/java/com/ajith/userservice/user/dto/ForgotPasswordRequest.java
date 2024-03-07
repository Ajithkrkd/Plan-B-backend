package com.ajith.userservice.user.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ForgotPasswordRequest {
    private String token;
    private String newPassword;
}
