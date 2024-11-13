package org.example.issuecoupon.domain.dto;

public record ValidateCouponIssueRequest(
        Long couponId,
        Long maxQuantity,
        Long eventId,
        String userId
) {
    public static ValidateCouponIssueRequest of(Long couponId, Long maxQuantity, Long eventId, String userId) {
        return new ValidateCouponIssueRequest(couponId, maxQuantity, eventId, userId);
    }
}
