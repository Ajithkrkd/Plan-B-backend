package com.ajith.apigateway.utils;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    public static final List<String>WHITELIST = List.of (
            "/user/api/auth/register",
            "/user/api/auth/login",
            "/user/api/auth/confirm-email",
            "/eureka",
            "/user/api/auth/get_forgot_password_link",
            "/user/api/auth/forgot_password"
    );

    public Predicate< ServerHttpRequest> isSecured =
            serverHttpRequest -> WHITELIST
                    .stream ()
                    .noneMatch ( uri ->serverHttpRequest.getURI ().getPath ().startsWith ( uri ) );
}
