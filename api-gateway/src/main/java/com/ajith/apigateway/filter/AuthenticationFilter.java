package com.ajith.apigateway.filter;


import com.ajith.apigateway.exceptions.AuthHeaderNotFountException;
import com.ajith.apigateway.exceptions.TokenInvalidException;
import com.ajith.apigateway.jwtUtils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component

public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private  RouteValidator validator;

    @Autowired
    private JwtUtils jwtUtils;
    public AuthenticationFilter (){
        super(Config.class);
    }

    @Override
    public GatewayFilter apply (Config config) {
        return ((exchange, chain) -> {

            if(validator.isSecured.test ( exchange.getRequest () ))
            {
                try { //header contain token or not
                if(!exchange.getRequest ().getHeaders ().containsKey ( HttpHeaders.AUTHORIZATION ))
                {
                        throw new AuthHeaderNotFountException ( "missing authorization header" );
                }
                } catch (RuntimeException e) {
                    throw new AuthHeaderNotFountException ( "auth header is not fount please provide token" );
                }

                    String authHeader = exchange.getRequest ().getHeaders ().get ( HttpHeaders.AUTHORIZATION ).get ( 0 );
                    if(authHeader != null && authHeader.startsWith( "Bearer " )) {
                        authHeader = authHeader.substring ( 7 );
                    }

                try {
                    jwtUtils.validateToken (authHeader );
                }
                catch (Exception e)
                {
                    throw new TokenInvalidException ("token invalid or expired");
                }
            }
            return chain.filter ( exchange );
        });

    }
    public static class Config {

    }
}
