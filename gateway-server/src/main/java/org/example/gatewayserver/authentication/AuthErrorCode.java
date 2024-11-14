package org.example.gatewayserver.authentication;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode {

    NOT_TOKEN(UNAUTHORIZED, "토큰이 존재하지 않습니다."),
    EXPIRED_TOKEN(UNAUTHORIZED, "토큰이 만료되었습니다. 다시 로그인해주세요."),
    INVALID_TOKEN(UNAUTHORIZED, "유효하지 않은 토큰입니다.");

    private final HttpStatus status;
    private final String message;
}
