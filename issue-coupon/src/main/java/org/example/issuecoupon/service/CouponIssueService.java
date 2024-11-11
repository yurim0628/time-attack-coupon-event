package org.example.issuecoupon.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.coupon.domain.Coupon;
import org.example.coupon.service.CouponService;
import org.example.issuecoupon.domain.CouponIssue;
import org.example.issuecoupon.domain.SaveCouponIssueRequest;
import org.example.issuecoupon.service.port.CouponIssueRepository;
import org.example.redis.domain.CouponCache;
import org.example.redis.service.CouponIssueRedisService;
import org.example.redis.service.CouponRedisService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponIssueService {

    private final CouponService couponService;
    private final CouponRedisService couponCacheService;
    private final CouponIssueRedisService couponIssueCacheService;
    private final CouponIssueRepository couponIssueRepository;

    @Transactional
    public void issueCoupon(SaveCouponIssueRequest saveCouponIssueRequest) {
        Long couponId = saveCouponIssueRequest.couponId();
        Long userId = saveCouponIssueRequest.userId();

        log.info("Issuing Coupon. " +
                "Coupon ID: [{}], User ID: [{}]", couponId, userId);
        CouponCache cachedCoupon = getCouponCache(couponId);
        validateCouponIssue(cachedCoupon, String.valueOf(userId));

        saveCouponIssue(couponId, userId);
        log.info("Coupon Issued Successfully. " +
                "Coupon ID: [{}], User ID: [{}]", couponId, userId);
    }

    private CouponCache getCouponCache(Long couponId) {
        return couponCacheService.getCoupon(couponId)
                .orElseGet(() -> fetchCouponFromDbAndCache(couponId));
    }

    private CouponCache fetchCouponFromDbAndCache(Long couponId) {
        log.info("Cache miss. Fetching Coupon from Database and Caching." +
                "Coupon ID: [{}]", couponId);

        Coupon coupon = couponService.getCoupon(couponId);
        CouponCache couponCache = CouponCache.of(
                coupon.getId(),
                coupon.getMaxQuantity(),
                coupon.getEventId()
        );

        couponCacheService.saveCoupon(couponCache);
        return couponCache;
    }

    private void validateCouponIssue(CouponCache cachedCoupon, String userId) {
        couponIssueCacheService.checkCouponIssueQuantityAndDuplicate(cachedCoupon, userId);
    }

    private void saveCouponIssue(Long couponId, Long userId) {
        log.info("Saving Coupon Issue. " +
                "Coupon ID: [{}], User ID: [{}]", couponId, userId);
        CouponIssue couponIssue = CouponIssue.of(couponId, userId);
        couponIssueRepository.save(couponIssue);
    }
}
