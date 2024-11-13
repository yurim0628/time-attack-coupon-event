package org.example.issuecoupon.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.issuecoupon.domain.dto.SaveCouponIssueRequest;
import org.example.issuecoupon.service.CouponIssueService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/coupons")
@RequiredArgsConstructor
public class CouponIssueController {

    private final CouponIssueService couponIssueService;

    @PostMapping("/issues")
    public ResponseEntity<Void> issueCoupon(@RequestBody @Valid SaveCouponIssueRequest saveCouponIssueRequest) {
        couponIssueService.issueCoupon(saveCouponIssueRequest);
        return ResponseEntity.ok().build();
    }
}
