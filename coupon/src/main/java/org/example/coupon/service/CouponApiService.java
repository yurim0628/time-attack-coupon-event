package org.example.coupon.service;

import lombok.RequiredArgsConstructor;
import org.example.coupon.service.port.CouponApiClient;
import org.springframework.stereotype.Component;

import static org.example.coupon.utils.RequestUrlUtils.*;

@Component
@RequiredArgsConstructor
public class CouponApiService {

    private final CouponApiClient couponApiClient;

    public Long requestGetIssuedCouponCount(Long couponId) {
        String getIssuedCouponCountUrl = buildUriWithPathVariable(
                REDIS_SERVICE_URL,
                GET_ISSUED_COUPON_COUNT_ENDPOINT,
                couponId
        );
        return couponApiClient.requestGetIssuedCouponCount(getIssuedCouponCountUrl);
    }
}
