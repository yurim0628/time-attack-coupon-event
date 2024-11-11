package org.example.redis.infrastructure.vo;

import lombok.Builder;
import lombok.Getter;
import org.example.redis.domain.CouponCache;

@Getter
public class CouponRedisVO {

    private final Long id;
    private final Long maxQuantity;
    private final Long eventId;

    @Builder
    private CouponRedisVO(Long id, Long maxQuantity, Long eventId) {
        this.id = id;
        this.maxQuantity = maxQuantity;
        this.eventId = eventId;
    }

    public static CouponRedisVO fromModel(CouponCache couponCache) {
        return CouponRedisVO.builder()
                .id(couponCache.getId())
                .maxQuantity(couponCache.getMaxQuantity())
                .eventId(couponCache.getEventId())
                .build();
    }

    public CouponCache toModel() {
        return CouponCache.builder()
                .id(id)
                .maxQuantity(maxQuantity)
                .eventId(eventId)
                .build();
    }
}
