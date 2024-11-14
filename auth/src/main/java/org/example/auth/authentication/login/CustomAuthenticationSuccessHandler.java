package org.example.auth.authentication.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.auth.authentication.PrincipalDetails;
import org.example.auth.authentication.token.Token;
import org.example.auth.authentication.token.TokenAuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final ObjectMapper objectMapper;
    private final TokenAuthenticationService tokenAuthenticationService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        Token token = tokenAuthenticationService.createToken(
                principalDetails.getUserId(),
                principalDetails.getAuthorities().toString()
        );
        sendSuccessResponse(response, LoginResponse.from(token));
    }

    private void sendSuccessResponse(HttpServletResponse response, LoginResponse loginResponse)
            throws IOException {
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(UTF_8.name());
        response.setStatus(HttpStatus.OK.value());
        objectMapper.writeValue(
                response.getWriter(),
                loginResponse
        );
    }
}
