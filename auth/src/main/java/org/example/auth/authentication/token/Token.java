package org.example.auth.authentication.token;

import lombok.Builder;

@Builder
public record Token(
        String grantType,
        String accessToken
) {
}
