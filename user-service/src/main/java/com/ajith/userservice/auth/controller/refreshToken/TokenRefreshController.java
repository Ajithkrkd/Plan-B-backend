package com.ajith.userservice.auth.controller.refreshToken;

import com.ajith.userservice.auth.dto.LoginResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TokenRefreshController {

    private final RefreshTokenService refreshTokenService;

    @PostMapping("/user/api/auth/refreshToken")
    public LoginResponse refreshToken(HttpServletRequest request, HttpServletResponse response){
        return refreshTokenService.refreshToken(request, response);
    }
}
