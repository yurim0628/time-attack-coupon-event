package org.example.issuecoupon.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.issuecoupon.domain.Coupon;
import org.example.issuecoupon.domain.CouponCache;
import org.example.issuecoupon.domain.CouponIssue;
import org.example.issuecoupon.domain.dto.SaveCouponIssueRequest;
import org.example.issuecoupon.exception.IssueCouponException;
import org.example.issuecoupon.service.port.CouponIssueRepository;
import org.springframework.stereotype.Service;

import static org.example.issuecoupon.exception.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponIssueService {

    private final CouponIssueApiService couponIssueApiService;
    private final CouponIssueRepository couponIssueRepository;

    @Transactional
    public void issueCoupon(SaveCouponIssueRequest saveCouponIssueRequest) {
        Long couponId = saveCouponIssueRequest.couponId();
        Long userId = saveCouponIssueRequest.userId();
        log.info("Issuing Coupon. Coupon ID: [{}], User ID: [{}]", couponId, userId);

        CouponCache cachedCoupon = getCouponFromCache(couponId);
        validateCouponIssue(cachedCoupon, String.valueOf(userId));

        saveCouponIssue(couponId, userId);
        log.info("Coupon Issued Successfully. Coupon ID: [{}], User ID: [{}]", couponId, userId);
    }

    private CouponCache getCouponFromCache(Long couponId) {
        log.info("Getting Coupons from Cache. Coupon ID: [{}]", couponId);
        return couponIssueApiService.requestGetCouponFromCache(couponId)
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
        couponIssueApiService.requestSaveCouponFromCache(couponCache);

        return couponCache;
    }

    private void validateCouponIssue(CouponCache cachedCoupon, String userId) {
        log.info("Validating Coupon Issue. Coupon ID: [{}], User ID: [{}]", cachedCoupon.getId(), userId);
        String result = couponIssueApiService.requestCouponValidation(cachedCoupon, userId);

        if ("COUPON_ISSUE_QUANTITY_EXCEEDED".equals(result)) {
            throw new IssueCouponException(COUPON_ISSUE_QUANTITY_EXCEEDED);
        } else if ("COUPON_ALREADY_ISSUED_BY_USER".equals(result)) {
            throw new IssueCouponException(COUPON_ALREADY_ISSUED_BY_USER);
        }
    }

    private void saveCouponIssue(Long couponId, Long userId) {
        log.info("Saving Coupon Issue. Coupon ID: [{}], User ID: [{}]", couponId, userId);
        CouponIssue couponIssue = CouponIssue.of(couponId, userId);
        couponIssueRepository.save(couponIssue);
    }
}
