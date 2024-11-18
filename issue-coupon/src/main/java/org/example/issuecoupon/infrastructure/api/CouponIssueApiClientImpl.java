package org.example.issuecoupon.infrastructure.api;

import lombok.RequiredArgsConstructor;
import org.example.issuecoupon.domain.Coupon;
import org.example.issuecoupon.domain.dto.GetCouponResponse;
import org.example.issuecoupon.service.api.port.CouponIssueApiClient;
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
}
