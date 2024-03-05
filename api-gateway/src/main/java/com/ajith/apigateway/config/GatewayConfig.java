package com.ajith.apigateway.config;

import com.ajith.apigateway.filter.GatewayUriPathLogger;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {
    @Bean
    public GlobalFilter globalFilter(){
        return new GatewayUriPathLogger ();
    }
}