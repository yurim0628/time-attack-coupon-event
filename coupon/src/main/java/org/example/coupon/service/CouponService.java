package org.example.coupon.service;

import lombok.RequiredArgsConstructor;
import org.example.coupon.domain.Coupon;
import org.example.coupon.domain.GetCouponResponse;
import org.example.coupon.exception.CouponException;
import org.example.coupon.service.port.CouponRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.example.coupon.exception.ErrorCode.COUPON_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;

    @Transactional(readOnly = true)
    public GetCouponResponse getCoupon(Long couponId) {
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new CouponException(COUPON_NOT_FOUND));
        return GetCouponResponse.from(coupon);
    }

    @Transactional(readOnly = true)
    public List<Coupon> getAllCoupons() {
        return couponRepository.findAll();
    }
}
