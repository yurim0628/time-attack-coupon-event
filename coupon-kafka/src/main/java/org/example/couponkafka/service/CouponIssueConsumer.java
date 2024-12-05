package org.example.couponkafka.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.couponkafka.domain.CouponIssue;
import org.example.couponkafka.service.port.CouponIssueRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponIssueConsumer {

    private final CouponIssueRepository couponIssueRepository;

    @KafkaListener(
            topics = "TimeAttackCouponIssue",
            groupId = "Coupon-TimeAttackCouponIssue"
    )
    public void listener(CouponIssue couponIssue, Acknowledgment acknowledgment) {
        try {
            saveCouponIssue(couponIssue);
            acknowledgment.acknowledge();
        } catch (Exception e) {
            log.error("Failed to Save CouponIssue: [{}]", couponIssue, e);
            throw new RuntimeException("Message Processing Failed", e);
        }
    }

    private void saveCouponIssue(CouponIssue couponIssue) {
        couponIssueRepository.save(couponIssue);
    }
}
