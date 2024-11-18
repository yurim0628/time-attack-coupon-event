package org.example.issuecoupon.service.cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.issuecoupon.domain.CouponCache;
import org.example.issuecoupon.exception.IssueCouponException;
import org.example.issuecoupon.service.cache.cache.CouponIssueCacheStore;
import org.springframework.stereotype.Component;

import static org.example.issuecoupon.exception.ErrorCode.COUPON_ALREADY_ISSUED_BY_USER;
import static org.example.issuecoupon.exception.ErrorCode.COUPON_ISSUE_QUANTITY_EXCEEDED;
import static org.example.issuecoupon.utils.RedisKeyUtils.getCouponIssueRequestKey;

@Slf4j
@Component
@RequiredArgsConstructor
public class CouponIssueCacheService {

    private final CouponIssueCacheStore couponIssueCacheStore;

    public void checkCouponIssueQuantityAndDuplicate(CouponCache cachedCoupon, String userId) {
        Long couponId = cachedCoupon.getId();
        String maxQuantity = String.valueOf(cachedCoupon.getMaxQuantity());
        log.info("Checking Total Issued Coupon Quantity and Duplicate Issuance." +
                "Coupon ID: [{}], User ID: [{}]", couponId, userId);

        String couponIssueRequestKey = getCouponIssueRequestKey(couponId);
        Long result = couponIssueCacheStore.checkCouponIssueAvailability(couponIssueRequestKey, maxQuantity, userId);

        switch (result.intValue()) {
            case 0 -> log.info("Coupon Issued Successfully. " +
                    "USER ID: [{}], Coupon ID: [{}]", userId, couponId);
            case 1 -> throw new IssueCouponException(COUPON_ISSUE_QUANTITY_EXCEEDED);
            case 2 -> throw new IssueCouponException(COUPON_ALREADY_ISSUED_BY_USER);
        }
    }
}
