package org.example.issuecoupon.service.port;

import org.example.issuecoupon.domain.CouponIssue;

public interface CouponIssueRepository {

    CouponIssue save(CouponIssue couponIssue);

    boolean existsByCouponIdAndUserId(Long couponId, Long userId);
}
