package org.example.redis.infrastructure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.redis.exception.RedisException;
import org.example.redis.service.port.CommonCacheStore;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import static org.example.redis.exception.RedisErrorCode.COMMON_SYSTEM_ERROR;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommonCacheStoreImpl implements CommonCacheStore {

    private final StringRedisTemplate redisTemplate;

    /**
     * Redis에서 세션 콜백을 실행하는 메서드.
     * 트랜잭션 작업 또는 여러 명령어를 한 세션에서 처리할 때 사용.
     *
     * @param sessionCallback 실행할 세션 콜백
     * @param <T> 세션 콜백 실행의 결과 타입
     * @throws RedisException 세션 콜백 실행 중 예외 발생 시 공통 예외로 처리
     */
    @Override
    public <T> void execute(SessionCallback<T> sessionCallback) {
        log.info("Executing Redis Session Callback");
        try {
            redisTemplate.execute(sessionCallback);
            log.info("Redis Session Callback Executed Successfully.");
        } catch (Exception e) {
            log.error("Redis Session Callback Exception", e);
            throw new RedisException("Failed to Execute Redis Session Callback", COMMON_SYSTEM_ERROR);
        }
    }
}
