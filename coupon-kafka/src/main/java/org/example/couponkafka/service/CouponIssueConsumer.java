package org.example.couponkafka.service;

import lombok.RequiredArgsConstructor;
import org.example.couponkafka.domain.CouponIssue;
import org.example.couponkafka.service.port.CouponIssueRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponIssueConsumer {

    private final CouponIssueRepository couponIssueRepository;

    @KafkaListener(
            topics = "TimeAttackCouponIssue",
            groupId = "Coupon-TimeAttackCouponIssue"
    )
    public void listener(CouponIssue couponIssue, Acknowledgment acknowledgment) {
        saveCouponIssue(couponIssue);
        acknowledgment.acknowledge();
    }

    private void saveCouponIssue(CouponIssue couponIssue) {
        couponIssueRepository.save(couponIssue);
    }
}
