package org.example.redis.infrastructure;

import lombok.RequiredArgsConstructor;
import org.example.redis.service.port.CouponIssueCacheStore;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CouponIssueCacheStoreImpl implements CouponIssueCacheStore {

    private final CouponIssueRedisCache couponIssueRedisCache;

    @Override
    public Long getIssuedCouponUserCount(String issueRequestKey) {
        return couponIssueRedisCache.getIssuedCouponUserCount(issueRequestKey);
    }

    @Override
    public Long addIssuedCouponUser(String issueRequestKey, String userId) {
        return couponIssueRedisCache.addIssuedCouponUser(issueRequestKey, userId);
    }
}
