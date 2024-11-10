package org.example.auth.authentication.login;

import lombok.Builder;
import org.example.auth.authentication.token.Token;

@Builder
public record LoginResponse(
        Token token
) {
    public static LoginResponse from(Token token) {
        return LoginResponse.builder()
                .token(token)
                .build();
    }
}
