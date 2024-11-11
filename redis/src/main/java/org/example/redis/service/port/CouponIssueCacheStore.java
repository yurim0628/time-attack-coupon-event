package org.example.redis.service.port;

public interface CouponIssueCacheStore {

    Long checkCouponIssueAvailability(String couponIssueRequestKey, String maxQuantity, String userId);
}
