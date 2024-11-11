package org.example.redis.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.redis.domain.CouponCache;
import org.example.redis.service.port.CouponCacheStore;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static org.example.redis.utils.RedisKeyUtils.getCouponKey;

@Slf4j
@Component
@RequiredArgsConstructor
public class CouponRedisService {

    private final CouponCacheStore couponCacheStore;

    public Optional<CouponCache> getCoupon(Long couponId) {
        String couponKey = getCouponKey(couponId);
        log.info("Getting Coupon. " +
                "Coupon ID: [{}], Generated Key: [{}]", couponId, couponKey);
        return couponCacheStore.getCoupon(couponKey);
    }

    public void saveCoupon(CouponCache couponCache) {
        Long couponId = couponCache.getId();
        String couponKey = getCouponKey(couponId);
        log.info("Saving Coupon " +
                "Coupon ID: [{}], Key: [{}]", couponId, couponKey);
        couponCacheStore.saveCoupon(couponKey, couponCache);
    }
}
