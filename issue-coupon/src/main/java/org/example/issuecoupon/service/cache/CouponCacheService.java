package org.example.issuecoupon.service.cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.issuecoupon.domain.CouponCache;
import org.example.issuecoupon.service.cache.cache.CouponCacheStore;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static org.example.issuecoupon.utils.RedisKeyUtils.getCouponKey;

@Slf4j
@Component
@RequiredArgsConstructor
public class CouponCacheService {

    private final CouponCacheStore couponCacheStore;

    public void saveCoupon(CouponCache couponCache) {
        Long couponId = couponCache.getId();
        String saveCouponKey = getCouponKey(couponId);
        log.info("Saving Coupon. Coupon ID: [{}], Key: [{}]", couponId, saveCouponKey);
        couponCacheStore.saveCoupon(saveCouponKey, couponCache);
    }

    public Optional<CouponCache> getCoupon(Long couponId) {
        String getCouponKey = getCouponKey(couponId);
        log.info("Getting Coupon. Coupon ID: [{}], Key: [{}]", couponId, getCouponKey);
        return couponCacheStore.getCoupon(getCouponKey);
    }
}
