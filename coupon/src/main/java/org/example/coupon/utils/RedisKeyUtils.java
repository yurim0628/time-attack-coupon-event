package org.example.coupon.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class RedisKeyUtils {

    private static final String COUPON_ISSUE_REQUEST_PREFIX = "coupon:issue:request:couponId=%s:users";

    public static String getCouponIssueRequestKey(Long couponId) {
        return COUPON_ISSUE_REQUEST_PREFIX.formatted(couponId);
    }
}
