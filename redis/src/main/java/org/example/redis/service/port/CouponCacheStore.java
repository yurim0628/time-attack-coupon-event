package org.example.redis.service.port;

import org.example.redis.domain.CouponCache;

import java.util.Optional;

public interface CouponCacheStore {

    Optional<CouponCache> getCoupon(String key);

    void saveCoupon(String key, CouponCache couponCache);
}
