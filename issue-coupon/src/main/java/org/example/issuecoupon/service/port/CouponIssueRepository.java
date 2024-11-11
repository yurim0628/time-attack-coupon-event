package org.example.issuecoupon.service.port;

import org.example.issuecoupon.domain.CouponIssue;

public interface CouponIssueRepository {

    void save(CouponIssue couponIssue);
}
