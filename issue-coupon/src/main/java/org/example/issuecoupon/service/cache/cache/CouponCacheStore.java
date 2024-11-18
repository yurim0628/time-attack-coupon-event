package org.example.issuecoupon.service.cache.cache;

import org.example.issuecoupon.domain.CouponCache;

import java.util.Optional;

public interface CouponCacheStore {

    void saveCoupon(String saveCouponKey, CouponCache couponCache);

    Optional<CouponCache> getCoupon(String getCouponKey);
}
