package org.example.gatewayserver.authentication;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
public class TokenAuthenticationProvider {

    private final Key key;

    public TokenAuthenticationProvider(@Value("${jwt.secret-key}") String secretKey) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    public String getSubject(String token) throws ExpiredJwtException, SignatureException,
            MalformedJwtException, UnsupportedJwtException, IllegalArgumentException {
        return getClaims(token).getSubject();
    }

    public Claims getClaims(String token) throws ExpiredJwtException, SignatureException,
            MalformedJwtException, UnsupportedJwtException, IllegalArgumentException {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
