package org.example.redis.exception;

import lombok.Getter;

@Getter
public class RedisException extends RuntimeException {

    private final RedisErrorCode redisErrorCode;

    public RedisException(String message, RedisErrorCode redisErrorCode) {
        super(message);
        this.redisErrorCode = redisErrorCode;
    }

    public RedisException(RedisErrorCode redisErrorCode) {
        super(redisErrorCode.getMessage());
        this.redisErrorCode = redisErrorCode;
    }
}
