package org.example.couponkafka.service;

import lombok.RequiredArgsConstructor;
import org.example.couponkafka.domain.CouponIssue;
import org.example.couponkafka.service.port.CouponIssueRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponIssueConsumer {

    private final CouponIssueRepository couponIssueRepository;

    @KafkaListener(topics = "topic", groupId = "coupon_issue_consumer_group")
    public void listener(CouponIssue couponIssue) {
        saveCouponIssue(couponIssue);
    }

    private void saveCouponIssue(CouponIssue couponIssue) {
        couponIssueRepository.save(couponIssue);
    }
}
