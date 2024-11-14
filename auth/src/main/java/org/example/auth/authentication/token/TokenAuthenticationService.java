package org.example.auth.authentication.token;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static org.example.auth.authentication.constant.SecurityConstants.BEARER_TYPE;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenAuthenticationService {

    private final TokenAuthenticationProvider tokenAuthenticationProvider;

    public Token createToken(String userId, String authorities) {
        String accessToken = tokenAuthenticationProvider.generateAccessToken(userId, authorities);
        log.info("Access Token Generated for USER ID: [{}]", userId);
        return Token.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .build();
    }
}
