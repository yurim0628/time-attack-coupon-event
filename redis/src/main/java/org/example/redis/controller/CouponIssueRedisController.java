package org.example.redis.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.redis.domain.dto.ValidateCouponIssueRequest;
import org.example.redis.service.CouponIssueRedisService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/redis/coupon-issues")
@RequiredArgsConstructor
public class CouponIssueRedisController {

    private final CouponIssueRedisService couponIssueRedisService;

    @PostMapping("/validate")
    public ResponseEntity<Void> validateCouponIssue(
            @RequestBody @Valid ValidateCouponIssueRequest validateCouponIssueRequest) {
        couponIssueRedisService.checkCouponIssueQuantityAndDuplicate(validateCouponIssueRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{couponId}/count")
    public ResponseEntity<Long> getCouponIssueCount(@PathVariable Long couponId) {
        Long issueCount = couponIssueRedisService.getIssuedCouponCount(couponId);
        return ResponseEntity.ok(issueCount);
    }
}
