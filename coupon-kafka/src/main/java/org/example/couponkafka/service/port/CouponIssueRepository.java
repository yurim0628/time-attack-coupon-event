package org.example.couponkafka.service.port;

import org.example.couponkafka.domain.CouponIssue;

public interface CouponIssueRepository {

    void save(CouponIssue couponIssue);
}
