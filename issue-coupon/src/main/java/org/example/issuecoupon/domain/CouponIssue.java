package org.example.issuecoupon.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static org.example.issuecoupon.domain.CouponStatus.ACTIVE;

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

    public static CouponIssue of(Long couponId, String userId) {
        return CouponIssue.builder()
                .couponStatus(ACTIVE)
                .couponId(couponId)
                .userId(Long.parseLong(userId))
                .build();
    }
}
