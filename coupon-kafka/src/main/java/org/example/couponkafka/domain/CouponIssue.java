package org.example.couponkafka.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CouponIssue {

    private Long id;
    private CouponStatus couponStatus;
    private Long couponId;
    private Long userId;

    @Builder
    private CouponIssue(Long id, CouponStatus couponStatus, Long couponId, Long userId) {
        this.id = id;
        this.couponStatus = couponStatus;
        this.couponId = couponId;
        this.userId = userId;
    }
}
