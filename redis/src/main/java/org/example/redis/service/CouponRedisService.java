package org.example.redis.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.redis.domain.CouponCache;
import org.example.redis.domain.dto.GetCouponCacheResponse;
import org.example.redis.domain.dto.SaveCouponCacheRequest;
import org.example.redis.exception.RedisException;
import org.example.redis.service.port.CouponCacheStore;
import org.springframework.stereotype.Component;

import static org.example.redis.exception.RedisErrorCode.COUPON_NOT_FOUND;
import static org.example.redis.utils.RedisKeyUtils.getCouponKey;

@Slf4j
@Component
@RequiredArgsConstructor
public class CouponRedisService {

    private final CouponCacheStore couponCacheStore;

    public void saveCoupon(SaveCouponCacheRequest saveCouponCacheRequest) {
        Long couponId = saveCouponCacheRequest.couponId();
        String saveCouponKey = getCouponKey(couponId);
        log.info("Saving Coupon Coupon ID: [{}], Key: [{}]", couponId, saveCouponKey);

        CouponCache couponCache = CouponCache.of(
                saveCouponCacheRequest.couponId(),
                saveCouponCacheRequest.maxQuantity(),
                saveCouponCacheRequest.eventId()
        );
        couponCacheStore.saveCoupon(saveCouponKey, couponCache);
    }

    public GetCouponCacheResponse getCoupon(Long couponId) {
        String getCouponKey = getCouponKey(couponId);
        log.info("Getting coupon Coupon ID: [{}], Key: [{}]", couponId, getCouponKey);

        CouponCache couponCache = couponCacheStore.getCoupon(getCouponKey)
                .orElseThrow(() -> new RedisException(COUPON_NOT_FOUND));
        return GetCouponCacheResponse.from(couponCache);
    }
}
