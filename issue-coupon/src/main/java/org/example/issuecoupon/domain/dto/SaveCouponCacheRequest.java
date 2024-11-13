package org.example.issuecoupon.domain.dto;

public record SaveCouponCacheRequest(
        Long couponId,
        Long maxQuantity,
        Long eventId
) {
    public static SaveCouponCacheRequest of(Long couponId, Long maxQuantity, Long eventId) {
        return new SaveCouponCacheRequest(couponId, maxQuantity, eventId);
    }
}
