package org.example.issuecoupon.domain.dto;

public record GetCouponResponse(
        Long couponId,
        Long maxQuantity,
        Long eventId
) {
}
