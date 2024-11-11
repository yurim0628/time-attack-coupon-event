package org.example.redis.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.redis.exception.RedisException;
import org.example.redis.infrastructure.vo.CouponRedisVO;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static org.example.redis.exception.RedisErrorCode.COMMON_SYSTEM_ERROR;

@Slf4j
@Component
@RequiredArgsConstructor
public class CouponRedisCache {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    public Optional<CouponRedisVO> getCoupon(String key) {
        log.info("Find Redis Key = [{}]", key);
        String serializedValue = redisTemplate.opsForValue().get(key);
        try {
            return Optional.of(objectMapper.readValue(serializedValue, CouponRedisVO.class));
        } catch (IllegalArgumentException | InvalidFormatException e) {
            log.error("Failed to Deserialize Coupon for key [{}]", key, e);
            return Optional.empty();
        } catch (Exception e) {
            log.error("Redis Get Exception for key [{}]", key, e);
            throw new RedisException("Redis get() Error", COMMON_SYSTEM_ERROR);
        }
    }

    /**
     * 쿠폰 객체를 Redis에 저장하는 메서드.
     * 주어진 쿠폰 객체를 CouponRedisEntity로 변환하여 직렬화 후 Redis에 저장.
     *
     * @param key Redis에 저장될 쿠폰 데이터의 키.
     * @param couponRedisVO Redis에 저장할 쿠폰 객체.
     * @throws RedisException 직렬화 또는 Redis 저장 중 예외 발생 시 공통 예외로 처리.
     */
    public void setCoupon(String key, CouponRedisVO couponRedisVO) {
        log.info("Save Redis Key = [{}], Value = [{}]", key, couponRedisVO);
        try {
            String serializedValue = objectMapper.writeValueAsString(couponRedisVO);
            redisTemplate.opsForValue().set(key, serializedValue);
        } catch (Exception e) {
            log.error("Redis Set Exception", e);
            throw new RedisException("Redis set() Error", COMMON_SYSTEM_ERROR);
        }
    }
}
