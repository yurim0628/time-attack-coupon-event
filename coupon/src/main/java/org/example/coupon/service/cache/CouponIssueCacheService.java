package org.example.coupon.service.cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.coupon.service.cache.port.CouponIssueCacheStore;
import org.springframework.stereotype.Component;

import static org.example.coupon.utils.RedisKeyUtils.getCouponIssueRequestKey;

@Slf4j
@Component
@RequiredArgsConstructor
public class CouponIssueCacheService {

    private final CouponIssueCacheStore couponIssueCacheStore;

    public Long getIssuedCouponCount(Long couponId) {
        String issuedCountKey = getCouponIssueRequestKey(couponId);
        return couponIssueCacheStore.getIssuedCouponUserCount(issuedCountKey);
    }
}
