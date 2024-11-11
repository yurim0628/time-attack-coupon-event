package org.example.redis.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class RedisKeyUtils {

    private static final String COUPON_PREFIX = "coupon:couponId=%s";
    private static final String COUPON_ISSUE_REQUEST_PREFIX = "coupon:issue:request:couponId=%s:users";

    public static String getCouponKey(Long couponId) {
        return COUPON_PREFIX.formatted(couponId);
    }

    public static String getCouponIssueRequestKey(Long couponId) {
        return COUPON_ISSUE_REQUEST_PREFIX.formatted(couponId);
    }
}
