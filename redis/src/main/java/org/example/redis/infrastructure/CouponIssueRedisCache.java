package org.example.redis.infrastructure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.redis.exception.RedisException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static org.example.redis.exception.RedisErrorCode.COMMON_SYSTEM_ERROR;

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
     * @throws RedisException Redis에서 사용자 수 조회 중 예외가 발생할 경우 공통 예외로 처리.
     */
    public Long getIssuedCouponUserCount(String key) {
        try {
            Long count = redisTemplate.opsForSet().size(key);
            return Objects.requireNonNullElse(count, NO_USERS_ISSUED);
        } catch (Exception e) {
            log.error("Failed to Get Issued Coupon User Count for Key: [{}]", key, e);
            throw new RedisException("Failed to Get Issued Coupon User Count", COMMON_SYSTEM_ERROR);
        }
    }

    /**
     * Redis의 SADD 명령어를 사용하여 특정 쿠폰에 대해 사용자를 추가.
     *
     * @param key 쿠폰 발급 사용자 데이터를 저장하는 Redis 집합의 키.
     * @param value 발급받은 사용자를 나타내는 식별자.
     * @return 사용자가 쿠폰 집합에 성공적으로 추가되면 true, 그렇지 않다면 false.
     * @throws RedisException Redis에서 사용자 추가 중 예외가 발생할 경우 공통 예외로 처리.
     */
    public Long addIssuedCouponUser(String key, String value) {
        try {
            Long result = redisTemplate.opsForSet().add(key, value);
            return Objects.requireNonNullElse(result, NO_USERS_ISSUED);
        } catch (Exception e) {
            log.error("Failed to Add Issued Coupon User for Key: [{}], Value: [{}]", key, value, e);
            throw new RedisException("Failed to Add Issued Coupon User", COMMON_SYSTEM_ERROR);
        }
    }
}
