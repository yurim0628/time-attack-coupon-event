package org.example.issuecoupon.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.issuecoupon.domain.dto.SaveCouponIssueRequest;
import org.example.issuecoupon.service.CouponIssueService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.example.issuecoupon.AccessTokenProvider.setAccessToken;

@RestController
@RequestMapping("/coupon-issues")
@RequiredArgsConstructor
public class CouponIssueController {

    private final CouponIssueService couponIssueService;

    public static final String USER_ID_HEADER = "X-User-Id";

    @PostMapping
    public ResponseEntity<Void> issueCoupon(
            @RequestBody @Valid SaveCouponIssueRequest saveCouponIssueRequest,
            @RequestHeader(USER_ID_HEADER) String userId,
            HttpServletRequest request
    ) {
        setAccessToken(request);
        couponIssueService.issueCoupon(saveCouponIssueRequest, userId);
        return ResponseEntity.ok().build();
    }
}
