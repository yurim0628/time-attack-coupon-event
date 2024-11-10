package org.example.issuecoupon.service;

import org.example.issuecoupon.domain.SaveCouponIssueRequest;
import org.example.issuecoupon.exception.IssueCouponException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootTest
public class CouponIssueServiceTest {

    @Autowired
    private CouponIssueService couponIssueService;

    @Test
    void issueCouponConcurrencyTest() throws InterruptedException {
        int threadCount = 100;

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            long userId = i;
            executorService.submit(() -> {
                try {
                    SaveCouponIssueRequest saveCouponIssueRequest = new SaveCouponIssueRequest(userId, 1L, 1L);
                    couponIssueService.issueCoupon(saveCouponIssueRequest);
                    successCount.incrementAndGet();
                } catch (IssueCouponException e) {
                    failCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        System.out.println("Issue Coupon Success Count: " + successCount.get());
        System.out.println("Issue Coupon Fail Count: " + failCount.get());
    }
}
