package org.example.issuecoupon.service;

import lombok.RequiredArgsConstructor;
import org.example.issuecoupon.domain.Coupon;
import org.example.issuecoupon.domain.CouponCache;
import org.example.issuecoupon.domain.dto.SaveCouponCacheRequest;
import org.example.issuecoupon.domain.dto.ValidateCouponIssueRequest;
import org.example.issuecoupon.service.port.CouponIssueApiClient;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static org.example.issuecoupon.utils.RequestUrlUtils.*;

@Component
@RequiredArgsConstructor
public class CouponIssueApiService {

    private final CouponIssueApiClient couponIssueApiClient;

    public Optional<Coupon> requestGetCouponFromDb(Long couponId) {
        String getCouponFromDbUrl = buildUriWithPathVariable(
                COUPON_SERVICE_URL,
                GET_COUPONS_FROM_DB_ENDPOINT,
                couponId
        );
        return couponIssueApiClient.requestGetCouponFromDb(getCouponFromDbUrl);
    }

    public Optional<CouponCache> requestGetCouponFromCache(Long couponId) {
        String getCouponFromCacheUrl = buildUriWithPathVariable(
                REDIS_SERVICE_URL,
                GET_COUPONS_FROM_CACHE_ENDPOINT,
                couponId
        );
        return couponIssueApiClient.requestGetCouponFromCache(getCouponFromCacheUrl);
    }

    public void requestSaveCouponFromCache(CouponCache couponCache) {
        Long couponId = couponCache.getId();
        String saveCouponFromCacheUrl = buildUri(
                REDIS_SERVICE_URL,
                SAVE_COUPONS_FROM_CACHE_ENDPOINT
        );
        SaveCouponCacheRequest saveCouponCacheRequest = SaveCouponCacheRequest.of(
                couponId,
                couponCache.getMaxQuantity(),
                couponCache.getEventId()
        );
        couponIssueApiClient.requestSaveCouponFromCache(saveCouponFromCacheUrl, saveCouponCacheRequest);
    }

    public String requestCouponValidation(CouponCache cachedCoupon, String userId) {
        String validateIssueCouponUrl = buildUri(
                REDIS_SERVICE_URL,
                VALIDATE_ISSUE_COUPONS_ENDPOINT
        );
        ValidateCouponIssueRequest validateCouponIssueRequest = ValidateCouponIssueRequest.of(
                cachedCoupon.getId(),
                cachedCoupon.getMaxQuantity(),
                cachedCoupon.getEventId(),
                userId
        );
        return couponIssueApiClient.requestCouponValidation(validateIssueCouponUrl, validateCouponIssueRequest);
    }
}
