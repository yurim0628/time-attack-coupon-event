package org.example.auth.user.domain.dto;

public record RegisterRequest(
        String email,
        String password
) {
}
