package org.example.auth.authentication.login;

public record LoginRequest(
        String email,
        String password
) {
}
