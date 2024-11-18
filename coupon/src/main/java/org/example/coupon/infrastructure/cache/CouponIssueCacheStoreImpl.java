package org.example.coupon.infrastructure.cache;

import lombok.RequiredArgsConstructor;
import org.example.coupon.service.cache.port.CouponIssueCacheStore;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CouponIssueCacheStoreImpl implements CouponIssueCacheStore {

    private final CouponIssueRedisCache couponIssueRedisCache;

    @Override
    public Long getIssuedCouponUserCount(String issueRequestKey) {
        return couponIssueRedisCache.getIssuedCouponUserCount(issueRequestKey);
    }
}
