package org.example.redis.domain.dto;

public record SaveCouponCacheRequest(
        Long couponId,
        Long maxQuantity,
        Long eventId
) {
}
