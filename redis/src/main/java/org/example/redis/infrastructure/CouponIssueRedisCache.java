package org.example.redis.infrastructure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static java.util.Collections.singletonList;

@Slf4j
@Component
@RequiredArgsConstructor
public class CouponIssueRedisCache {

    private final static Long NO_USERS_ISSUED = 0L;
    private final StringRedisTemplate redisTemplate;
    private final DefaultRedisScript<Long> couponUserCountAndAddScript;

    /**
     * 쿠폰 발급 가능 여부를 확인.
     * Lua 스크립트를 사용하여 발급된 사용자 수가 최대 수량을 초과했는지,
     * 사용자에게 이미 발급된 쿠폰이 있는지 확인한 후, 해당 사용자에게 쿠폰 발급을 시도.
     *
     * @param couponIssueRequestKey 쿠폰 발급 요청을 나타내는 Redis 키
     * @param maxQuantity           최대 발급 수량
     * @param userId                사용자 ID
     * @return 0: 쿠폰 발급 성공, 1: 발급 수량 초과, 2: 이미 발급된 사용자
     */
    public Long checkCouponIssueAvailability(String couponIssueRequestKey, String maxQuantity, String userId) {
        return redisTemplate.execute(
                couponUserCountAndAddScript,
                singletonList(couponIssueRequestKey),
                maxQuantity, userId
        );
    }

    /**
     * Redis의 SCARD 명령어를 사용하여 특정 쿠폰에 대해 발급된 사용자 수를 확인.
     *
     * @param key 쿠폰이 발급된 사용자 데이터를 저장하는 Redis 집합의 키.
     * @return 해당 쿠폰을 발급받은 사용자 수. 존재하지 않을 경우 0을 반환.
     */
    public Long getIssuedCouponUserCount(String key) {
        Long count = redisTemplate.opsForSet().size(key);
        return Objects.requireNonNullElse(count, NO_USERS_ISSUED);
    }
}
