package org.example.issuecoupon.service.api;

import lombok.RequiredArgsConstructor;
import org.example.issuecoupon.domain.Coupon;
import org.example.issuecoupon.service.api.port.CouponIssueApiClient;
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
}
