package org.example.issuecoupon.service.api.port;

import org.example.issuecoupon.domain.Coupon;

import java.util.Optional;

public interface CouponIssueApiClient {

    Optional<Coupon> requestGetCouponFromDb(String getCouponFromDbUrl);
}
