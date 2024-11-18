package org.example.coupon.service.cache.port;

public interface CouponIssueCacheStore {

    Long getIssuedCouponUserCount(String issuedCountKey);
}
