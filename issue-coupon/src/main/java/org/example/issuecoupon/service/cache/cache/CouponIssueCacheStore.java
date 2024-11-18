package org.example.issuecoupon.service.cache.cache;

public interface CouponIssueCacheStore {

    Long checkCouponIssueAvailability(String couponIssueRequestKey, String maxQuantity, String userId);
}
