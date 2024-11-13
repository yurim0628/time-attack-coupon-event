package org.example.redis.domain.dto;

import org.example.redis.domain.CouponCache;

public record GetCouponCacheResponse(
        Long couponId,
        Long maxQuantity,
        Long eventId
)
{
    public static GetCouponCacheResponse from(CouponCache couponCache) {
        return new GetCouponCacheResponse(
                couponCache.getId(),
                couponCache.getMaxQuantity(),
                couponCache.getEventId()
        );
    }
}
