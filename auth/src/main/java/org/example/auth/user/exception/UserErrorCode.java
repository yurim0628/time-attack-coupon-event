package org.example.auth.user.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@Getter
public enum UserErrorCode {
    COMMON_SYSTEM_ERROR(INTERNAL_SERVER_ERROR, "서버에 내부 오류가 발생했습니다. 요청을 처리하는 동안 예상치 못한 문제가 발생했습니다."),
    COMMON_JSON_PROCESSING_ERROR(BAD_REQUEST, "JSON 처리 중 문제가 발생했습니다. 데이터 형식이 잘못되었거나 유효하지 않은 JSON 형식입니다."),
    COMMON_RESOURCE_NOT_FOUND(NOT_FOUND, "요청한 리소스를 찾을 수 없습니다. 요청한 URL에 해당하는 리소스가 없거나 삭제되었을 수 있습니다."),
    METHOD_ARGUMENT_NOT_VALID(BAD_REQUEST, null),
    INVALID_REQUEST(BAD_REQUEST, "데이터 요청 형식이 올바르지 않습니다."),

    EMAIL_ALREADY_EXISTS(CONFLICT, "이미 사용 중인 이메일 입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
