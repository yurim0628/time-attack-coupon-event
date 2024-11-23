package org.example.issuecoupon.service.business;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.issuecoupon.domain.Coupon;
import org.example.issuecoupon.domain.CouponCache;
import org.example.issuecoupon.domain.CouponIssue;
import org.example.issuecoupon.domain.dto.SaveCouponIssueRequest;
import org.example.issuecoupon.exception.IssueCouponException;
import org.example.issuecoupon.service.api.CouponIssueApiService;
import org.example.issuecoupon.service.cache.CouponCacheService;
import org.example.issuecoupon.service.cache.CouponIssueCacheService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static org.example.issuecoupon.exception.ErrorCode.COUPON_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponIssueService {

    private final CouponIssueApiService couponIssueApiService;
    private final CouponIssueCacheService couponIssueCacheService;
    private final CouponCacheService couponCacheService;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Transactional
    public void issueCoupon(SaveCouponIssueRequest saveCouponIssueRequest, String userId) {
        Long couponId = saveCouponIssueRequest.couponId();
        log.info("Issuing Coupon. Coupon ID: [{}], User ID: [{}]", couponId, userId);

        CouponCache cachedCoupon = getCouponFromCache(couponId);
        validateCouponIssue(cachedCoupon, userId);

        saveCouponIssue(couponId, userId);
        log.info("Coupon Issued Successfully. Coupon ID: [{}], User ID: [{}]", couponId, userId);
    }

    private CouponCache getCouponFromCache(Long couponId) {
        log.info("Getting Coupons from Cache. Coupon ID: [{}]", couponId);
        return couponCacheService.getCoupon(couponId)
                .orElseGet(() -> getCouponFromDbAndCache(couponId));
    }

    private CouponCache getCouponFromDbAndCache(Long couponId) {
        log.info("Cache Miss. Getting Coupon from Database. Coupon ID: [{}]", couponId);
        Coupon coupon = couponIssueApiService.requestGetCouponFromDb(couponId)
                .orElseThrow(() -> new IssueCouponException(COUPON_NOT_FOUND));

        log.info("Saving Coupon to Cache. Coupon ID: [{}]", couponId);
        CouponCache couponCache = CouponCache.of(
                coupon.getId(),
                coupon.getMaxQuantity(),
                coupon.getEventId()
        );
        couponCacheService.saveCoupon(couponCache);

        return couponCache;
    }

    private void validateCouponIssue(CouponCache cachedCoupon, String userId) {
        log.info("Validating Coupon Issue. Coupon ID: [{}], User ID: [{}]", cachedCoupon.getId(), userId);
        couponIssueCacheService.checkCouponIssueQuantityAndDuplicate(cachedCoupon, userId);
    }

    private void saveCouponIssue(Long couponId, String userId) {
        log.info("Saving Coupon Issue. Coupon ID: [{}], User ID: [{}]", couponId, userId);
        CouponIssue couponIssue = CouponIssue.of(couponId, userId);
        kafkaTemplate.send("topic", couponIssue);
    }
}
