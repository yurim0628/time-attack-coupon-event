package org.example.issuecoupon.infrastructure.cache;

import lombok.RequiredArgsConstructor;
import org.example.issuecoupon.service.cache.cache.CouponIssueCacheStore;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CouponIssueCacheStoreImpl implements CouponIssueCacheStore {

    private final CouponIssueRedisCache couponIssueRedisCache;

    @Override
    public Long checkCouponIssueAvailability(String couponIssueRequestKey, String maxQuantity, String userId) {
        return couponIssueRedisCache.checkCouponIssueAvailability(couponIssueRequestKey, maxQuantity, userId);
    }
}
