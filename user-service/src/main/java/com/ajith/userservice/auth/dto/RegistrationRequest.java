package com.ajith.userservice.auth.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationRequest {
    private String fullName;
    private String phoneNumber;
    private String email;
    private String password;
}
