package org.example.coupon.service;

import lombok.RequiredArgsConstructor;
import org.example.coupon.domain.Coupon;
import org.example.coupon.exception.CouponException;
import org.example.coupon.service.port.CouponRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.example.coupon.exception.ErrorCode.COUPON_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;

    @Transactional(readOnly = true)
    public Coupon getCoupon(Long couponId) {
        return couponRepository.findById(couponId)
                .orElseThrow(() -> new CouponException(COUPON_NOT_FOUND));
    }

    @Transactional
    public void saveCoupon(Coupon coupon) {
        couponRepository.save(coupon);
    }
}
