package org.example.redis.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import static java.util.Collections.singletonList;

@Component
@RequiredArgsConstructor
public class CouponIssueRedisCache {

    private final StringRedisTemplate redisTemplate;

    /**
     * 쿠폰 발급 가능 여부를 확인하는 메서드.
     * Lua 스크립트를 사용하여 발급된 사용자 수가 최대 수량을 초과했는지,
     * 사용자에게 이미 발급된 쿠폰이 있는지 확인한 후, 해당 사용자에게 쿠폰 발급을 시도합니다.
     *
     * @param couponIssueRequestKey 쿠폰 발급 요청을 나타내는 Redis 키
     * @param maxQuantity           최대 발급 수량
     * @param userId                사용자 ID
     * @return 0: 쿠폰 발급 성공, 1: 발급 수량 초과, 2: 이미 발급된 사용자
     */
    public Long checkCouponIssueAvailability(String couponIssueRequestKey, String maxQuantity, String userId) {
        String script =
                "local max_quantity = tonumber(ARGV[1]) " +
                        "local user_id = ARGV[2] " +
                        "local issued_count = redis.call('SCARD', KEYS[1]) " +
                        "if issued_count >= max_quantity then " +
                        "    return 1 " +
                        "end " +
                        "if redis.call('SISMEMBER', KEYS[1], user_id) == 1 then " +
                        "    return 2 " +
                        "end " +
                        "redis.call('SADD', KEYS[1], user_id) " +
                        "return 0";

        return redisTemplate.execute(
                new DefaultRedisScript<>(script, Long.class),
                singletonList(couponIssueRequestKey),
                maxQuantity, userId
        );
    }
}
