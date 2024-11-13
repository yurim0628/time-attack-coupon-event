package org.example.coupon.domain;

public record GetCouponResponse(
        Long couponId,
        Long maxQuantity,
        Long eventId
) {
    public static GetCouponResponse from(Coupon coupon) {
        return new GetCouponResponse(
                coupon.getId(),
                coupon.getMaxQuantity(),
                coupon.getEventId()
        );
    }
}
