package com.ajith.userservice.config;

import com.ajith.userservice.user.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig{

    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthFilter authFilter;
    private static final String[] WHITE_LIST_URLS =
            {
                    "/user/api/auth/**",
                    "/uploads/**",
                    "/v3/api-docs",
                    "/v3/api-docs/**",
                    "/v2/api-docs",
                    "/swagger-resources",
                    "/swagger-resources/**",
                    "/configuration/ui",
                    "/configuration/security",
                    "/swagger-ui/**",
                    "/webjars/**",
                    "/swagger-ui.html"
            };
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests ( auth ->{
                    auth.requestMatchers (WHITE_LIST_URLS )
                            .permitAll ()
                            .requestMatchers ( "/user/api/secure/" ).hasAnyAuthority ( Role.USER.name () )
                            .anyRequest ().authenticated ();
                })
                .authenticationProvider ( authenticationProvider )
                .addFilterBefore ( authFilter , UsernamePasswordAuthenticationFilter.class)
                .build ();

    }
}
