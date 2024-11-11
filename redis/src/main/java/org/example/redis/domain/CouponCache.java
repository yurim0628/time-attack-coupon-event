package org.example.redis.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CouponCache {

    private final Long id;
    private final Long maxQuantity;
    private final Long eventId;

    @Builder
    private CouponCache(Long id, Long maxQuantity, Long eventId) {
        this.id = id;
        this.maxQuantity = maxQuantity;
        this.eventId = eventId;
    }

    public static CouponCache of(Long id, Long maxQuantity, Long eventId) {
        return CouponCache.builder()
                .id(id)
                .maxQuantity(maxQuantity)
                .eventId(eventId)
                .build();
    }
}
