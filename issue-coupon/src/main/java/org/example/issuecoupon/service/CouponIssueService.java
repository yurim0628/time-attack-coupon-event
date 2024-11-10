package org.example.issuecoupon.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.coupon.domain.Coupon;
import org.example.coupon.service.CouponService;
import org.example.issuecoupon.domain.CouponIssue;
import org.example.issuecoupon.domain.SaveCouponIssueRequest;
import org.example.issuecoupon.exception.IssueCouponException;
import org.example.issuecoupon.service.port.CouponIssueRepository;
import org.springframework.stereotype.Service;

import static org.example.issuecoupon.exception.ErrorCode.COUPON_ALREADY_ISSUED_BY_USER;
import static org.example.issuecoupon.exception.ErrorCode.COUPON_ISSUE_QUANTITY_EXCEEDED;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponIssueService {

    private final CouponService couponService;
    private final CouponIssueRepository couponIssueRepository;

    @Transactional
    public void issueCoupon(SaveCouponIssueRequest saveCouponIssueRequest) {
        Long couponId = saveCouponIssueRequest.couponId();
        Long userId = saveCouponIssueRequest.userId();
        log.info("Issuing Coupon. " +
                "Coupon ID: [{}], User ID: [{}]", couponId, userId);

        if (!isTotalIssueQuantityAvailable(couponId)) {
            throw new IssueCouponException(COUPON_ISSUE_QUANTITY_EXCEEDED);
        }
        if (isUserAlreadyIssuedCoupon(couponId, userId)) {
            throw new IssueCouponException(COUPON_ALREADY_ISSUED_BY_USER);
        }

        saveCouponIssue(couponId, userId);
        incrementIssuedQuantity(couponId);
        log.info("Coupon Issued Successfully. " +
                "Coupon ID: [{}], User ID: [{}]", couponId, userId);
    }

    private Coupon getCoupon(Long couponId) {
        return couponService.getCoupon(couponId);
    }

    private boolean isTotalIssueQuantityAvailable(Long couponId) {
        Coupon coupon = getCoupon(couponId);
        Long maxQuantity = coupon.getMaxQuantity();
        Long issuedQuantity = coupon.getIssuedQuantity();
        log.info("Checking Total Issue Quantity. " +
                "Issued Count: [{}], Max Quantity: [{}]", issuedQuantity, maxQuantity);
        return issuedQuantity < maxQuantity;
    }

    private boolean isUserAlreadyIssuedCoupon(Long couponId, Long userId) {
        log.info("Verifying User Coupon Issue Status. " +
                "Coupon ID: [{}], User ID: [{}]", couponId, userId);
        return couponIssueRepository.existsByCouponIdAndUserId(couponId, userId);
    }

    private void saveCouponIssue(Long couponId, Long userId) {
        log.info("Saving Coupon Issue Request. " +
                "Coupon ID: [{}], User ID: [{}]", couponId, userId);
        CouponIssue couponIssue = CouponIssue.of(couponId, userId);
        couponIssueRepository.save(couponIssue);
    }

    private void incrementIssuedQuantity(Long couponId) {
        Coupon coupon = getCoupon(couponId);
        coupon.incrementIssuedQuantity();
        couponService.saveCoupon(coupon);
    }
}
