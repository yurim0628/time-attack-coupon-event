package org.example.redis.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.redis.domain.dto.GetCouponCacheResponse;
import org.example.redis.domain.dto.SaveCouponCacheRequest;
import org.example.redis.service.CouponRedisService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/redis/coupons")
@RequiredArgsConstructor
public class CouponRedisController {

    private final CouponRedisService couponRedisService;

    @GetMapping("/{couponId}")
    public ResponseEntity<GetCouponCacheResponse> getCoupon(@PathVariable Long couponId) {
        return ResponseEntity.ok(couponRedisService.getCoupon(couponId));
    }

    @PostMapping
    public ResponseEntity<Void> saveCoupon(@RequestBody @Valid SaveCouponCacheRequest saveCouponCacheRequest) {
        couponRedisService.saveCoupon(saveCouponCacheRequest);
        return ResponseEntity.ok().build();
    }
}
