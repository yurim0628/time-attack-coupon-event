package org.example.coupon.infrastructure.business.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.coupon.domain.Coupon;
import org.example.coupon.domain.DiscountType;

import java.time.LocalDateTime;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
@Table(name = "coupons")
public class CouponEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Enumerated(STRING)
    private DiscountType discountType;
    private Long discountRate;
    private Long discountPrice;
    private Long maxQuantity;
    private Long issuedQuantity;
    private LocalDateTime validateStartDate;
    private LocalDateTime validateEndDate;

    private Long eventId;

    public static CouponEntity fromModel(Coupon coupon) {
        CouponEntity couponEntity = new CouponEntity();
        couponEntity.id = coupon.getId();
        couponEntity.discountType = coupon.getDiscountType();
        couponEntity.discountRate = coupon.getDiscountRate();
        couponEntity.discountPrice = coupon.getDiscountPrice();
        couponEntity.maxQuantity = coupon.getMaxQuantity();
        couponEntity.issuedQuantity = coupon.getIssuedQuantity();
        couponEntity.validateStartDate = coupon.getValidateStartDate();
        couponEntity.validateEndDate = coupon.getValidateEndDate();
        couponEntity.eventId = coupon.getEventId();
        return couponEntity;
    }

    public Coupon toModel() {
        return Coupon.builder()
                .id(id)
                .discountType(discountType)
                .discountRate(discountRate)
                .discountPrice(discountPrice)
                .maxQuantity(maxQuantity)
                .issuedQuantity(issuedQuantity)
                .validateStartDate(validateStartDate)
                .validateEndDate(validateEndDate)
                .eventId(eventId)
                .build();
    }
}
