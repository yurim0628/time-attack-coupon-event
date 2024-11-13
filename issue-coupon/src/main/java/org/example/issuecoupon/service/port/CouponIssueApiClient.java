package org.example.issuecoupon.service.port;

import org.example.issuecoupon.domain.Coupon;
import org.example.issuecoupon.domain.CouponCache;
import org.example.issuecoupon.domain.dto.SaveCouponCacheRequest;
import org.example.issuecoupon.domain.dto.ValidateCouponIssueRequest;

import java.util.Optional;

public interface CouponIssueApiClient {

    Optional<Coupon> requestGetCouponFromDb(String getCouponFromDbUrl);

    Optional<CouponCache> requestGetCouponFromCache(String getCouponFromCacheUrl);

    void requestSaveCouponFromCache(String saveCouponFromCacheUrl, SaveCouponCacheRequest request);

    String requestCouponValidation(String couponIssueValidationUrl, ValidateCouponIssueRequest validateCouponIssueRequest);
}
