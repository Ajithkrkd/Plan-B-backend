package com.ajith.userservice.user.token;

import com.ajith.userservice.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;
    public void revokeAllTokens (User user) {
        var validUserTokens = tokenRepository.findAllValidTokensByUser ( user.getId ( ) );
        if(validUserTokens.isEmpty()) {
            return;
        }
        else{
            validUserTokens.forEach ( t ->{
                t.setRevoked ( true );
                t.setExpired ( true );}
            );
            System.out.println ("Valid ---------------------------" );
            tokenRepository.saveAll ( validUserTokens );
        }
    }

    public void saveUserToken (User user, String jwtToken) {
            var token = Token.builder ( )
                    .user ( user )
                    .token ( jwtToken )
                    .tokenType ( TokenType.BEARER )
                    .isRefreshToken ( true )
                    .expired ( false )
                    .revoked ( false )
                    .build ();
            tokenRepository.save ( token );
    }
}
