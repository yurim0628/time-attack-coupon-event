package org.example.coupon.controller;

import lombok.RequiredArgsConstructor;
import org.example.coupon.domain.GetCouponResponse;
import org.example.coupon.service.business.CouponService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/coupons")
@RequiredArgsConstructor

public class CouponController {

    private final CouponService couponService;

    @GetMapping("/{couponId}")
    public ResponseEntity<GetCouponResponse> getCoupon(@PathVariable Long couponId) {
        return ResponseEntity.ok(couponService.getCoupon(couponId));
    }
}
