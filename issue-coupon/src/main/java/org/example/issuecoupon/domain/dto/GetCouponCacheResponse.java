package org.example.issuecoupon.domain.dto;

public record GetCouponCacheResponse(
        Long couponId,
        Long maxQuantity,
        Long eventId
) {
}
