package com.ajith.apigateway.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
@Slf4j
public class JwtUtils {

    @Value ( "${application.security.jwt.secret-key}" )
    private String secretKey;

    public boolean isTokenExpired (String token) {
        return extractExpiration(token).before(new Date ());
    }

    private  <T>T extractClaim (String token, Function <Claims,T> claimsResolver) {
        final Claims claims = extractAllClaims ( token );
        return claimsResolver.apply ( claims );
    }
    private Claims extractAllClaims (String token) {
        return Jwts
                .parserBuilder ()
                .setSigningKey ( getSigningKey() )
                .build ()
                .parseClaimsJws ( token )
                .getBody ();
    }

    private Date extractExpiration (String token) {
        return extractClaim ( token , Claims::getExpiration );
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    public boolean validateToken(String token){
        log.info("Token : {}",token);
        try {
            Jwts.parser().setSigningKey(getSigningKey ()).parseClaimsJws(token);
            return true;
        }
        catch (ExpiredJwtException e){
            throw new ExpiredJwtException(e.getHeader(),e.getClaims(),"Jwt token is expired");
        }
        catch (InvalidClaimException e){
            throw new JwtException ("Jwt token is invalid");
        }
        catch (Exception e){
            throw new RuntimeException("Something is wrong with the jwt token validation");
        }

    }
}
