package org.example.issuecoupon.domain.dto;

public record SaveCouponIssueRequest(
        Long eventId,
        Long couponId
) {
}
