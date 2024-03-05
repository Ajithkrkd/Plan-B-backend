package com.ajith.apigateway.filter;


import com.ajith.apigateway.exceptions.AuthHeaderNotFountException;
import com.ajith.apigateway.exceptions.TokenInvalidException;
import com.ajith.apigateway.utils.JwtUtils;
import com.ajith.apigateway.utils.RouteValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
@Slf4j
@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RouteValidator routeValidator;
    @Autowired
    private JwtUtils jwtUtils;
    public AuthenticationFilter (){
        super(Config.class);
    }

    @Override
    public GatewayFilter apply (Config config) {
        return ((exchange, chain) -> {
            log.info ( "inside gateway filter" );
            if( routeValidator.isSecured.test ( exchange.getRequest () ))
            {
                try
                {
                    if(!exchange.getRequest ().getHeaders ().containsKey ( HttpHeaders.AUTHORIZATION ))
                    {
                            throw new AuthHeaderNotFountException ( "missing authorization header" );
                    }
                    } catch (RuntimeException e) {
                    log.error ( "Exception" + e.getMessage () );
                        throw new AuthHeaderNotFountException ( "auth header is not fount please provide token" );
                    }

                    String authHeader = exchange.getRequest ().getHeaders ().get ( HttpHeaders.AUTHORIZATION ).get ( 0 );
                    if(authHeader != null && authHeader.startsWith( "Bearer " )) {
                        authHeader = authHeader.substring ( 7 );
                    }

                try
                {
                    log.info ( "checking token expired or not " + authHeader );
                    if(jwtUtils.isTokenExpired ( authHeader ))
                    {
                        log.info ( "token successfully accepected " + authHeader);
                    }
                }
                catch (Exception e)
                {
                    throw new RuntimeException ( e.getMessage () );
                }
            }
            return chain.filter ( exchange );
        });

    }
    public static class Config {

    }
}
