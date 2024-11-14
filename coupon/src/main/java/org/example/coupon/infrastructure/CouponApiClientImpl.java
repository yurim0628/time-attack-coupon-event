package org.example.coupon.infrastructure;

import lombok.RequiredArgsConstructor;
import org.example.coupon.service.port.CouponApiClient;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CouponApiClientImpl implements CouponApiClient {

    private final CouponRestTemplateClient issueCouponRestTemplateClient;

    @Override
    public Long requestGetIssuedCouponCount(String getIssuedCouponCountUrl) {
        return issueCouponRestTemplateClient.get(getIssuedCouponCountUrl, Long.class).get();
    }
}
