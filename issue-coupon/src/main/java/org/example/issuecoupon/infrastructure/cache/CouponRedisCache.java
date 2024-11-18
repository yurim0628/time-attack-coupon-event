package org.example.issuecoupon.infrastructure.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import io.lettuce.core.RedisException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.issuecoupon.exception.IssueCouponException;
import org.example.issuecoupon.infrastructure.cache.vo.CouponRedisVO;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static org.example.issuecoupon.exception.ErrorCode.COMMON_SYSTEM_ERROR;

@Slf4j
@Component
@RequiredArgsConstructor
public class CouponRedisCache {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    /**
     * Redis에서 특정 키로 저장된 쿠폰 데이터를 조회하는 메서드.
     * 키에 해당하는 데이터가 없거나 JSON 변환에 실패할 경우 빈 Optional을 반환.
     *
     * @param key Redis에 저장된 쿠폰 데이터의 키.
     * @return 조회된 쿠폰 데이터를 포함하는 Optional<CouponRedisEntity>.
     * @throws RedisException Redis에서 데이터 조회 중 예외 발생 시 공통 예외로 처리.
     */
    public Optional<CouponRedisVO> getCoupon(String key) {
        log.info("Find Redis Key = [{}]", key);
        String serializedValue = redisTemplate.opsForValue().get(key);
        if (serializedValue == null) {
            log.warn("Redis Key [{}] not found or value is null", key);
            return Optional.empty();
        }
        try {
            return Optional.of(objectMapper.readValue(serializedValue, CouponRedisVO.class));
        } catch (IllegalArgumentException | InvalidFormatException e) {
            log.error("Failed to Deserialize Coupon for key [{}]", key, e);
            return Optional.empty();
        } catch (Exception e) {
            log.error("Redis Get Exception for key [{}]", key, e);
            throw new IssueCouponException("Redis get() Error", COMMON_SYSTEM_ERROR);
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
            throw new IssueCouponException("Redis set() Error", COMMON_SYSTEM_ERROR);
        }
    }
}
