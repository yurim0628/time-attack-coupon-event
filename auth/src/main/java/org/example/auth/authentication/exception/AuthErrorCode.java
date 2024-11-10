package org.example.auth.authentication.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@Getter
public enum AuthErrorCode {
    INVALID_CREDENTIALS(UNAUTHORIZED, "아이디 또는 비밀번호를 확인해주세요."),
    INTERNAL_AUTHENTICATION_ERROR(INTERNAL_SERVER_ERROR, "로그인 중에 내부 서버 오류가 발생했습니다."),
    UNKNOWN_ERROR(INTERNAL_SERVER_ERROR, "로그인 중에 알 수 없는 오류가 발생했습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
