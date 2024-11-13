package org.example.redis.service.port;

import org.example.redis.domain.CouponCache;

import java.util.Optional;

public interface CouponCacheStore {

    void saveCoupon(String saveCouponKey, CouponCache couponCache);

    Optional<CouponCache> getCoupon(String getCouponKey);
}
