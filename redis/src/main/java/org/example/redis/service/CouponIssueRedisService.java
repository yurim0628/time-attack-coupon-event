package org.example.redis.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.redis.domain.CouponCache;
import org.example.redis.exception.RedisException;
import org.example.redis.service.port.CommonCacheStore;
import org.example.redis.service.port.CouponIssueCacheStore;
import org.jetbrains.annotations.NotNull;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Component;

import static org.example.redis.exception.RedisErrorCode.COUPON_ALREADY_ISSUED_BY_USER;
import static org.example.redis.exception.RedisErrorCode.COUPON_ISSUE_QUANTITY_EXCEEDED;
import static org.example.redis.utils.RedisKeyUtils.getCouponIssueRequestKey;

@Slf4j
@Component
@RequiredArgsConstructor
public class CouponIssueRedisService {

    private final static Long ADD_SUCCESS = 1L;

    private final CouponIssueCacheStore couponIssueCacheStore;
    private final CommonCacheStore commonCacheStore;

    public void checkCouponIssueQuantityAndDuplicate(CouponCache couponCache, String userId) {
        commonCacheStore.execute(new SessionCallback<>() {
            @Override
            public <K, V> Object execute(@NotNull RedisOperations<K, V> operations) throws DataAccessException {
                operations.multi();

                Long couponId = couponCache.getId();
                Long maxQuantity = couponCache.getMaxQuantity();
                log.info("Checking Total Issued Coupon Quantity and Duplicate Issuance." +
                        "Coupon ID: [{}], User ID: [{}]", couponId, userId);

                String couponIssueRequestKey = getCouponIssueRequestKey(couponId);
                if (!isTotalIssueQuantityAvailable(couponIssueRequestKey, maxQuantity)) {
                    throw new RedisException(COUPON_ISSUE_QUANTITY_EXCEEDED);
                }
                if (isUserAlreadyIssuedCoupon(couponIssueRequestKey, userId)) {
                    throw new RedisException(COUPON_ALREADY_ISSUED_BY_USER);
                }

                return operations.exec();
            }
        });
    }

    private boolean isTotalIssueQuantityAvailable(String couponIssueRequestKey, Long maxQuantity) {
        Long issuedCount = couponIssueCacheStore.getIssuedCouponUserCount(couponIssueRequestKey);
        log.info("Checking Total Issued Coupon Quantity. " +
                        "Issued Request Key: [{}], Issued Count: [{}], Max Quantity: [{}]",
                couponIssueRequestKey, issuedCount, maxQuantity);
        return issuedCount < maxQuantity;
    }

    public boolean isUserAlreadyIssuedCoupon(String issueRequestKey, String userId) {
        Long result = couponIssueCacheStore.addIssuedCouponUser(issueRequestKey, userId);
        log.debug("Verifying User Coupon Issue Status. " +
                "User ID: [{}], Issue Request Key: [{}], Result: [{}]", userId, issueRequestKey, result);
        return result >= ADD_SUCCESS;
    }
}
