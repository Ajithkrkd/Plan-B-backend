package com.ajith.userservice.auth.dto;

import com.ajith.userservice.utils.BasicResponse;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponse {
    private String access_token;
    private String refresh_token;
}
