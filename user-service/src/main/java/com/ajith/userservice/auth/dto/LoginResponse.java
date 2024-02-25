package com.ajith.userservice.auth.dto;

import com.ajith.userservice.utils.BasicResponse;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse extends BasicResponse {

    private String access_token;
    private String refresh_token;
}
