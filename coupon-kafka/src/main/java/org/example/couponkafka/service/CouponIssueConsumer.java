package org.example.couponkafka.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.couponkafka.domain.CouponIssue;
import org.example.couponkafka.service.port.CouponIssueRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.example.couponkafka.util.TransactionUtils.registerAfterCommit;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponIssueConsumer {

    private final CouponIssueRepository couponIssueRepository;

    @KafkaListener(
            topics = "TimeAttackCouponIssue",
            groupId = "Coupon-TimeAttackCouponIssue"
    )
    @Transactional
    public void listener(CouponIssue couponIssue, Acknowledgment acknowledgment) {
        try {
            log.info("Received CouponIssue: User ID: [{}], Coupon ID: [{}]",
                    couponIssue.getUserId(), couponIssue.getCouponId());
            saveCouponIssue(couponIssue);
            registerAfterCommit(acknowledgment::acknowledge);
        } catch (DataIntegrityViolationException e) {
            log.warn("Duplicate CouponIssue detected for User ID: [{}], Coupon ID: [{}]",
                    couponIssue.getUserId(), couponIssue.getCouponId());
            acknowledgment.acknowledge();
        } catch (Exception e) {
            throw new RuntimeException("Failed to Save CouponIssue. User ID: " + couponIssue.getUserId(), e);
        }
    }

    private void saveCouponIssue(CouponIssue couponIssue) {
        couponIssueRepository.save(couponIssue);
    }
}
