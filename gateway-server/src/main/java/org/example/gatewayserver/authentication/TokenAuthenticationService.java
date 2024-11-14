package org.example.gatewayserver.authentication;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenAuthenticationService {

    private final TokenAuthenticationProvider tokenAuthenticationProvider;

    public String validateAccessTokenAndGetSubject(String token) throws ExpiredJwtException, SignatureException,
            MalformedJwtException, UnsupportedJwtException, IllegalArgumentException {
        try {
            return tokenAuthenticationProvider.getSubject(token);
        } catch (ExpiredJwtException | SignatureException | MalformedJwtException |
                 UnsupportedJwtException | IllegalArgumentException e) {
            log.error("Token Validation Error. Error Message: [{}]", e.getMessage());
            throw e;
        }
    }
}
