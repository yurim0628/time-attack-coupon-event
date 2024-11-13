package org.example.redis.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.example.redis.exception.RedisErrorCode.*;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@RestControllerAdvice
public class RedisExceptionHandler {

    @ExceptionHandler(value = RedisException.class)
    public ResponseEntity<Map<String, String>> handleCustomException(RedisException e) {
        String name = e.getRedisErrorCode().name();
        HttpStatus status = e.getRedisErrorCode().getHttpStatus();
        String message = e.getRedisErrorCode().getMessage();

        log.error("[CustomException] Status: {}, Message: {}", status, message);

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("error", name);
        responseBody.put("message", message);

        return new ResponseEntity<>(responseBody, status);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();

        List<String> errorMessages = bindingResult.getFieldErrors().stream()
                .map(fieldError -> "[" + fieldError.getField() + "] " + fieldError.getDefaultMessage())
                .collect(Collectors.toList());

        String errorMessage = String.join(", ", errorMessages);

        log.error("[HandleMethodArgumentNotValidException] Message: {}", errorMessage);

        return ResponseEntity.badRequest().body(errorMessage);
    }

    @ExceptionHandler(value = NoResourceFoundException.class)
    public ResponseEntity<String> handleNoResourceFoundException(NoResourceFoundException e) {
        log.error("[NoResourceFoundException] URL = {}, Message = {}", e.getResourcePath(), e.getMessage());
        return new ResponseEntity<>(COMMON_RESOURCE_NOT_FOUND.getMessage(), NOT_FOUND);
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error("[HttpMessageNotReadableException] Message: {}", e.getMessage());
        return ResponseEntity.badRequest().body(COMMON_JSON_PROCESSING_ERROR.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        log.error("[Exception] Message: {}", e.getMessage(), e);
        return ResponseEntity.internalServerError().body(COMMON_SYSTEM_ERROR.getMessage());
    }
}
