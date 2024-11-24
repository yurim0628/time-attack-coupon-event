package org.example.issuecoupon.service.api;

import lombok.RequiredArgsConstructor;
import org.example.issuecoupon.domain.Coupon;
import org.example.issuecoupon.service.api.port.CouponIssueApiClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static org.example.issuecoupon.utils.RequestUrlUtils.*;

@Component
@RequiredArgsConstructor
public class CouponIssueApiService {

    @Value("${coupon-service.url}")
    private String couponServiceBaseUrl;

    @Value("${coupon-service.endpoints.get-coupons}")
    private String getCouponsEndpoint;

    private final CouponIssueApiClient couponIssueApiClient;

    public Optional<Coupon> requestGetCouponFromDb(Long couponId) {
        String getCouponFromDbUrl = buildUriWithPathVariable(
                couponServiceBaseUrl,
                getCouponsEndpoint,
                couponId
        );
        return couponIssueApiClient.requestGetCouponFromDb(getCouponFromDbUrl);
    }
}
