package org.example.gatewayserver.authentication;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.example.gatewayserver.authentication.AuthErrorCode.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static reactor.core.publisher.Mono.just;

@Component
@RequiredArgsConstructor
public class TokenAuthenticationFilter implements GlobalFilter {

    public static final String BEARER_PREFIX = "Bearer ";
    public static final int BEARER_PREFIX_LENGTH = BEARER_PREFIX.length();
    public static final String USER_ID_HEADER = "X-User-Id";

    private static final String REGISTER_PATH = "/users/sign-up";
    private static final String LOGIN_PATH = "/users/login";

    private final TokenAuthenticationService tokenAuthenticationService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        if (List.of(REGISTER_PATH, LOGIN_PATH).contains(path)) {
            return chain.filter(exchange);
        }

        String accessToken = getAccessToken(exchange);
        if (accessToken == null) {
            return handleErrorResponse(exchange, NOT_TOKEN);
        }

        try {
            String id = tokenAuthenticationService.validateAccessTokenAndGetSubject(accessToken);
            exchange.getRequest().mutate()
                    .header(USER_ID_HEADER, id)
                    .build();
        } catch (ExpiredJwtException e) {
            return handleErrorResponse(exchange, EXPIRED_TOKEN);
        } catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            return handleErrorResponse(exchange, INVALID_TOKEN);
        }

        return chain.filter(exchange);
    }

    private String getAccessToken(ServerWebExchange exchange) {
        String accessTokenHeader = exchange.getRequest().getHeaders().getFirst(AUTHORIZATION);

        if (accessTokenHeader != null && accessTokenHeader.startsWith(BEARER_PREFIX)) {
            return accessTokenHeader.substring(BEARER_PREFIX_LENGTH);
        }

        return null;
    }

    private Mono<Void> handleErrorResponse(ServerWebExchange exchange, AuthErrorCode authErrorCode) {
        return buildErrorResponse(exchange, authErrorCode.getMessage(), authErrorCode.getStatus());
    }

    private Mono<Void> buildErrorResponse(ServerWebExchange exchange, String message, HttpStatus status) {
        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(APPLICATION_JSON);

        byte[] messageBytes = message.getBytes();
        DataBuffer messageBuffer = exchange.getResponse()
                .bufferFactory()
                .wrap(messageBytes);

        return exchange.getResponse().writeWith(just(messageBuffer));
    }
}
