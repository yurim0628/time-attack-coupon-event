package org.example.issuecoupon.domain;

public record SaveCouponIssueRequest(
        Long userId,
        Long eventId,
        Long couponId
) {
}
