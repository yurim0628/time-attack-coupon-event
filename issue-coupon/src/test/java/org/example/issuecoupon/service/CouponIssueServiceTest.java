package org.example.issuecoupon.service;

import org.example.issuecoupon.domain.Coupon;
import org.example.issuecoupon.domain.CouponCache;
import org.example.issuecoupon.domain.CouponIssue;
import org.example.issuecoupon.domain.dto.SaveCouponIssueRequest;
import org.example.issuecoupon.exception.IssueCouponException;
import org.example.issuecoupon.service.port.CouponIssueRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.example.issuecoupon.domain.DiscountType.PERCENT;
import static org.example.issuecoupon.exception.ErrorCode.COUPON_ALREADY_ISSUED_BY_USER;
import static org.example.issuecoupon.exception.ErrorCode.COUPON_ISSUE_QUANTITY_EXCEEDED;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CouponIssueServiceTest {

    private final Long userId = 1L;
    private final Long eventId = 1L;
    private final Long couponId = 1L;
    private final Long maxQuantity = 100L;
    @Mock
    private CouponIssueApiService couponIssueApiService;
    @Mock
    private CouponIssueRepository couponIssueRepository;
    @InjectMocks
    private CouponIssueService couponIssueService;

    private SaveCouponIssueRequest createRequest() {
        return new SaveCouponIssueRequest(eventId, couponId);
    }

    private CouponCache createCouponCache() {
        return CouponCache.builder()
                .id(couponId)
                .maxQuantity(maxQuantity)
                .eventId(eventId)
                .build();
    }

    private Coupon createCoupon() {
        return Coupon.builder()
                .id(couponId)
                .discountType(PERCENT)
                .discountRate(10L)
                .maxQuantity(maxQuantity)
                .issuedQuantity(0L)
                .validateStartDate(LocalDateTime.now().minusDays(1))
                .validateEndDate(LocalDateTime.now().plusDays(10))
                .eventId(eventId)
                .build();
    }

    @Test
    @DisplayName("[SUCCESS] 쿠폰 발급이 성공적으로 이루어지는 경우")
    void issueCoupon_success() {
        // Given
        SaveCouponIssueRequest request = createRequest();
        CouponCache couponCache = createCouponCache();

        when(couponIssueApiService.requestGetCouponFromCache(couponId)).thenReturn(Optional.of(couponCache));
        when(couponIssueApiService.requestCouponValidation(any(CouponCache.class), anyString())).thenReturn("SUCCESS");

        // When
        couponIssueService.issueCoupon(request, "1L");

        // Then
        verify(couponIssueRepository).save(any(CouponIssue.class));
    }

    @Test
    @DisplayName("[ERROR] 쿠폰 발급 수량 초과로 예외 발생")
    void issueCoupon_QuantityExceeded_Exception() {
        // Given
        SaveCouponIssueRequest request = createRequest();
        CouponCache couponCache = createCouponCache();

        when(couponIssueApiService.requestGetCouponFromCache(couponId)).thenReturn(Optional.of(couponCache));
        when(couponIssueApiService.requestCouponValidation(any(CouponCache.class), anyString()))
                .thenReturn("COUPON_ISSUE_QUANTITY_EXCEEDED");

        // When & Then
        assertThrows(IssueCouponException.class, () -> couponIssueService.issueCoupon(request, "1L"), COUPON_ISSUE_QUANTITY_EXCEEDED.name());
    }

    @Test
    @DisplayName("[ERROR] 사용자가 이미 쿠폰을 발급받은 경우 예외 발생")
    void alreadyIssuedCouponByUser_Exception() {
        // Given
        SaveCouponIssueRequest request = createRequest();
        CouponCache couponCache = createCouponCache();

        when(couponIssueApiService.requestGetCouponFromCache(couponId)).thenReturn(Optional.of(couponCache));
        when(couponIssueApiService.requestCouponValidation(any(CouponCache.class), anyString()))
                .thenReturn("COUPON_ALREADY_ISSUED_BY_USER");

        // When & Then
        assertThrows(IssueCouponException.class, () -> couponIssueService.issueCoupon(request, "1L"), COUPON_ALREADY_ISSUED_BY_USER.name());
    }

    @Test
    @DisplayName("[SUCCESS] 캐시 미스 발생 시 DB에서 쿠폰 조회 및 캐시에 저장 후 발급")
    void cacheMiss_FetchFromDbAndStoreInCache() {
        // Given
        SaveCouponIssueRequest request = createRequest();
        Coupon coupon = createCoupon();

        when(couponIssueApiService.requestGetCouponFromCache(couponId)).thenReturn(Optional.empty());
        when(couponIssueApiService.requestGetCouponFromDb(couponId)).thenReturn(Optional.of(coupon));
        when(couponIssueApiService.requestCouponValidation(any(CouponCache.class), anyString())).thenReturn("SUCCESS");

        // When
        couponIssueService.issueCoupon(request, "1L");

        // Then
        verify(couponIssueApiService).requestSaveCouponFromCache(any(CouponCache.class));
        verify(couponIssueRepository).save(any(CouponIssue.class));
    }
}
