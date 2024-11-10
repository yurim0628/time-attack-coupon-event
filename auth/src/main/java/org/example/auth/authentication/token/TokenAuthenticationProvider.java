package org.example.auth.authentication.token;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

import static io.jsonwebtoken.SignatureAlgorithm.HS256;
import static org.example.auth.authentication.constant.SecurityConstants.ACCESS_TOKEN_EXPIRATION;
import static org.example.auth.authentication.constant.SecurityConstants.CLAIM_KEY;

@Component
public class TokenAuthenticationProvider {

    private final Key key;

    public TokenAuthenticationProvider(@Value("${jwt.secret-key}") String secretKey) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    public String generateAccessToken(String email, String authorities) {
        Date expirationDate = calculateTokenExpiration(ACCESS_TOKEN_EXPIRATION);
        return Jwts.builder()
                .setSubject(email)
                .setExpiration(expirationDate)
                .claim(CLAIM_KEY, authorities)
                .signWith(key, HS256)
                .compact();
    }

    public Date calculateTokenExpiration(Long expiration) {
        return new Date(System.currentTimeMillis() + expiration);
    }
}
