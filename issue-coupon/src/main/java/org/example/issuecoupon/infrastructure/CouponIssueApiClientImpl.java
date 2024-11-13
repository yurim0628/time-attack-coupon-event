package org.example.issuecoupon.infrastructure;

import lombok.RequiredArgsConstructor;
import org.example.issuecoupon.domain.Coupon;
import org.example.issuecoupon.domain.CouponCache;
import org.example.issuecoupon.domain.dto.GetCouponCacheResponse;
import org.example.issuecoupon.domain.dto.GetCouponResponse;
import org.example.issuecoupon.domain.dto.SaveCouponCacheRequest;
import org.example.issuecoupon.domain.dto.ValidateCouponIssueRequest;
import org.example.issuecoupon.service.port.CouponIssueApiClient;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CouponIssueApiClientImpl implements CouponIssueApiClient {

    private final IssueCouponRestTemplateClient issueCouponRestTemplateClient;

    @Override
    public Optional<Coupon> requestGetCouponFromDb(String getCouponFromDbUrl) {
        Optional<GetCouponResponse> getCouponResponse = issueCouponRestTemplateClient.get(
                getCouponFromDbUrl,
                GetCouponResponse.class
        );
        return getCouponResponse
                .map(Coupon::from)
                .or(Optional::empty);
    }

    @Override
    public Optional<CouponCache> requestGetCouponFromCache(String getCouponFromCacheUrl) {
        Optional<GetCouponCacheResponse> getCouponRedisResponse = issueCouponRestTemplateClient.get(
                getCouponFromCacheUrl,
                GetCouponCacheResponse.class
        );
        return getCouponRedisResponse
                .map(CouponCache::from)
                .or(Optional::empty);
    }

    @Override
    public void requestSaveCouponFromCache(String saveCouponFromCacheUrl, SaveCouponCacheRequest saveCouponCacheRequest) {
        issueCouponRestTemplateClient.post(
                saveCouponFromCacheUrl,
                saveCouponCacheRequest,
                Void.class
        );
    }

    @Override
    public String requestCouponValidation(String couponIssueValidationUrl, ValidateCouponIssueRequest validateCouponIssueRequest) {
        return issueCouponRestTemplateClient.post(
                couponIssueValidationUrl,
                validateCouponIssueRequest,
                String.class
        );
    }
}
