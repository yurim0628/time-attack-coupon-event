package org.example.coupon.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Coupon {

    private final Long id;
    private final DiscountType discountType;
    private final Long discountRate;
    private final Long discountPrice;
    private final Long maxQuantity;
    private final Long issuedQuantity;
    private final LocalDateTime validateStartDate;
    private final LocalDateTime validateEndDate;
    private final Long eventId;

    @Builder
    private Coupon(Long id, DiscountType discountType, Long discountRate, Long discountPrice,
                   Long maxQuantity, Long issuedQuantity, LocalDateTime validateStartDate,
                   LocalDateTime validateEndDate, Long eventId) {
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
}
