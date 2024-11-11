package org.example.redis.infrastructure;

import lombok.RequiredArgsConstructor;
import org.example.redis.service.port.CouponIssueCacheStore;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CouponIssueCacheStoreImpl implements CouponIssueCacheStore {

    private final CouponIssueRedisCache couponIssueRedisCache;

    @Override
    public Long checkCouponIssueAvailability(String couponIssueRequestKey, String maxQuantity, String userId) {
        return couponIssueRedisCache.checkCouponIssueAvailability(couponIssueRequestKey, maxQuantity, userId);
    }

    @Override
    public Long getIssuedCouponUserCount(String issueRequestKey) {
        return couponIssueRedisCache.getIssuedCouponUserCount(issueRequestKey);
    }
}
