package org.example.couponkafka.service.port;

import org.example.couponkafka.domain.CouponIssue;

public interface CouponIssueRepository {

    CouponIssue save(CouponIssue couponIssue);
}
