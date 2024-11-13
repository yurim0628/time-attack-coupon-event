package org.example.issuecoupon.domain.dto;

public record SaveCouponIssueRequest(
        Long userId,
        Long eventId,
        Long couponId
) {
}
