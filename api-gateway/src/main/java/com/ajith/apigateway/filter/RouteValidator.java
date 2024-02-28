package com.ajith.apigateway.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    public static final List<String>WHITELIST = List.of (
            "/api/auth/**",
            "/api/auth/login",
            "/api/auth/confirm-email/**",
            "/eureka"
    );

    public Predicate< ServerHttpRequest> isSecured =
            serverHttpRequest -> WHITELIST
                    .stream ()
                    .noneMatch ( uri ->serverHttpRequest.getURI ().getPath ().contains ( uri ) );
}
