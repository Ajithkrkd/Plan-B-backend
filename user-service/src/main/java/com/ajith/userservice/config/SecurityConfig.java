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

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig{

    private final AuthenticationProvider authenticationProvider;
    private static final String[] WHITE_LIST_URLS =
            {
                    "/api/auth/register/**",
                    "/api/auth/login/**",
                    "/api/auth/confirm-email/**"
            };
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {



        return http.csrf ( AbstractHttpConfigurer::disable )
                        .cors ( AbstractHttpConfigurer::disable )

                .authorizeHttpRequests ( auth ->{
                    auth.requestMatchers (WHITE_LIST_URLS )
                            .permitAll ()
                            .requestMatchers ( "/api/user/**" )
                            .hasAnyAuthority ( Role.USER.name () )
                            .anyRequest ()
                            .authenticated ();


                                        })
                .sessionManagement ( session->
                        session.sessionCreationPolicy ( SessionCreationPolicy.STATELESS ))
                                .authenticationProvider ( authenticationProvider )
                                                .build ();

    }
}
