package org.example.redis.domain.dto;

public record ValidateCouponIssueRequest(
        Long couponId,
        Long maxQuantity,
        Long eventId,
        String userId
) {
}
