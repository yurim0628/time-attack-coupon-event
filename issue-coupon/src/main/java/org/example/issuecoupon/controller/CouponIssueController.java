package org.example.issuecoupon.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.issuecoupon.domain.dto.SaveCouponIssueRequest;
import org.example.issuecoupon.service.business.CouponIssueService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/coupon-issues")
@RequiredArgsConstructor
public class CouponIssueController {

    private final CouponIssueService couponIssueService;

    public static final String USER_ID_HEADER = "X-User-Id";

    @PostMapping
    public ResponseEntity<Void> issueCoupon(
            @RequestBody @Valid SaveCouponIssueRequest saveCouponIssueRequest,
            @RequestHeader(USER_ID_HEADER) String userId
    ) {
        couponIssueService.issueCoupon(saveCouponIssueRequest, userId);
        return ResponseEntity.ok().build();
    }
}
