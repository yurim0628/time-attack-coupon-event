package org.example.coupon.service;

import org.example.coupon.domain.Coupon;
import org.example.coupon.service.port.CouponRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.example.coupon.domain.DiscountType.AMOUNT;
import static org.example.coupon.domain.DiscountType.PERCENT;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@EnableScheduling
public class CouponSyncSchedulerTest {

    @InjectMocks
    private CouponSyncScheduler couponSyncScheduler;

    @Mock
    private CouponService couponService;

    @Mock
    private CouponApiService couponApiService;

    @Mock
    private CouponRepository couponRepository;

    @Test
    @DisplayName("[SUCCESS] 쿠폰 발급 수량 업데이트가 성공적으로 처리된다")
    void issuedQuantityUpdate() {
        // Given
        Coupon coupon1 = Coupon.builder()
                .id(1L)
                .discountType(PERCENT)
                .discountRate(10L)
                .maxQuantity(100L)
                .issuedQuantity(50L)
                .validateStartDate(LocalDateTime.now())
                .validateEndDate(LocalDateTime.now().plusDays(10))
                .eventId(1L)
                .build();
        Coupon coupon2 = Coupon.builder()
                .id(2L)
                .discountType(AMOUNT)
                .discountPrice(1000L)
                .maxQuantity(200L)
                .issuedQuantity(150L)
                .validateStartDate(LocalDateTime.now())
                .validateEndDate(LocalDateTime.now().plusDays(10))
                .eventId(2L)
                .build();
        List<Coupon> coupons = Arrays.asList(coupon1, coupon2);

        when(couponService.getAllCoupons()).thenReturn(coupons);
        when(couponApiService.requestGetIssuedCouponCount(coupon1.getId())).thenReturn(50L);
        when(couponApiService.requestGetIssuedCouponCount(coupon2.getId())).thenReturn(150L);

        // When
        couponSyncScheduler.issuedQuantityUpdate();

        // Then
        verify(couponRepository, times(1)).updateIssuedQuantity(coupon1.getId(), 50L);
        verify(couponRepository, times(1)).updateIssuedQuantity(coupon2.getId(), 150L);
        verify(couponService, times(1)).getAllCoupons();
        verify(couponApiService, times(2)).requestGetIssuedCouponCount(anyLong());
    }

    @Test
    @DisplayName("[SUCCESS] 발급 수량이 없는 경우 업데이트가 발생하지 않는다")
    void noUpdateWhenZeroIssued() {
        // Given
        Coupon coupon1 = Coupon.builder()
                .id(1L)
                .discountType(PERCENT)
                .discountRate(10L)
                .maxQuantity(100L)
                .issuedQuantity(50L)
                .validateStartDate(LocalDateTime.now())
                .validateEndDate(LocalDateTime.now().plusDays(10))
                .eventId(1L)
                .build();
        Coupon coupon2 = Coupon.builder()
                .id(2L)
                .discountType(AMOUNT)
                .discountPrice(1000L)
                .maxQuantity(200L)
                .issuedQuantity(150L)
                .validateStartDate(LocalDateTime.now())
                .validateEndDate(LocalDateTime.now().plusDays(10))
                .eventId(2L)
                .build();
        List<Coupon> coupons = Arrays.asList(coupon1, coupon2);

        when(couponService.getAllCoupons()).thenReturn(coupons);
        when(couponApiService.requestGetIssuedCouponCount(coupon1.getId())).thenReturn(0L);
        when(couponApiService.requestGetIssuedCouponCount(coupon2.getId())).thenReturn(0L);

        // When
        couponSyncScheduler.issuedQuantityUpdate();

        // Then
        verify(couponRepository, never()).updateIssuedQuantity(anyLong(), anyLong());
        verify(couponService, times(1)).getAllCoupons();
        verify(couponApiService, times(2)).requestGetIssuedCouponCount(anyLong());
    }
}
