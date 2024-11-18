package org.example.issuecoupon.infrastructure.business.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.issuecoupon.domain.CouponIssue;
import org.example.issuecoupon.domain.CouponStatus;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@Table(name = "coupon_issues")
@NoArgsConstructor(access = PROTECTED)
public class CouponIssueEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Enumerated(STRING)
    private CouponStatus status;
    private Long couponId;
    private Long userId;

    public static CouponIssueEntity fromModel(CouponIssue couponIssue) {
        CouponIssueEntity couponIssueEntity = new CouponIssueEntity();
        couponIssueEntity.id = couponIssue.getId();
        couponIssueEntity.status = couponIssue.getCouponStatus();
        couponIssueEntity.couponId = couponIssue.getCouponId();
        couponIssueEntity.userId = couponIssue.getUserId();
        return couponIssueEntity;
    }

    public CouponIssue toModel() {
        return CouponIssue.builder()
                .id(id)
                .couponStatus(status)
                .couponId(couponId)
                .build();
    }
}
