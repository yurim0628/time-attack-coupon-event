package org.example.coupon.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Coupon {
    private Long id;
    private DiscountType discountType;
    private Long discountRate;
    private Long discountPrice;
    private Long maxQuantity;
    private Long issuedQuantity;
    private LocalDateTime validateStartDate;
    private LocalDateTime validateEndDate;
    private Long eventId;

    @Builder
    private Coupon(
            Long id,
            DiscountType discountType,
            Long discountRate,
            Long discountPrice,
            Long maxQuantity,
            Long issuedQuantity,
            LocalDateTime validateStartDate,
            LocalDateTime validateEndDate,
            Long eventId
    ) {
        this.id = id;
        this.discountType = discountType;
        this.discountRate = discountRate;
        this.discountPrice = discountPrice;
        this.maxQuantity = maxQuantity;
        this.issuedQuantity = issuedQuantity;
        this.validateStartDate = validateStartDate;
        this.validateEndDate = validateEndDate;
        this.eventId = eventId;
    }

    public void incrementIssuedQuantity() {
        issuedQuantity++;
    }
}
