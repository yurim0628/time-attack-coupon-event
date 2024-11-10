package org.example.auth.authentication.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.auth.authentication.exception.AuthErrorCode;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.example.auth.authentication.exception.AuthErrorCode.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        AuthErrorCode error;
        if (exception instanceof BadCredentialsException) {
            error = INVALID_CREDENTIALS;
        } else if (exception instanceof InternalAuthenticationServiceException) {
            error = INTERNAL_AUTHENTICATION_ERROR;
        } else {
            error = UNKNOWN_ERROR;
        }
        sendFailResponse(response, error);
    }

    private void sendFailResponse(HttpServletResponse response, AuthErrorCode authErrorCode)
            throws IOException {
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(UTF_8.name());
        response.setStatus(authErrorCode.getHttpStatus().value());
        objectMapper.writeValue(
                response.getWriter(),
                authErrorCode.getMessage()
        );
    }
}
