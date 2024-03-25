package com.ajith.projectservice.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.security.Key;

@Service
public class JwtUtils {
    private  String secretKey = "ebPOOal7zbI/R9kr7ueSpDOPLwGAHvgJ3CSZgAby7la72GQJAB86YH1tHBJ31Ofs";
    private  long jwtExpiration = 86400000L;
    private  long refreshExpiration = 604800000L;

    public    String getUsernameFromToken(String token) {
        Claims claims = (Claims) Jwts.parser().setSigningKey(getSigningKey()).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }
    private    Key getSigningKey() {
        byte[] keyBytes = (byte[]) Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public   String getJWTFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        return StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ") ? bearerToken.substring(7) : null;
    }

    public String getTokenFromAuthHeader (String authHeader) {
        return authHeader.substring(7);
    }

}
