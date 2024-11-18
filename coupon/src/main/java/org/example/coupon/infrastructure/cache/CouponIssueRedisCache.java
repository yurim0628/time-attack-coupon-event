package org.example.coupon.infrastructure.cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class CouponIssueRedisCache {

    private final static Long NO_USERS_ISSUED = 0L;
    private final StringRedisTemplate redisTemplate;

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
