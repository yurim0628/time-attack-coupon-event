package org.example.couponkafka.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.example.couponkafka.domain.CouponIssue;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DltMessageHandler {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void handleDltMessage(ConsumerRecord<String, CouponIssue> record) {
        log.error("Starting DLT Message Processing. Consumer Record: [{}]", record);
        log.error("Completed DLT Message Processing. CouponIssue Sent: [{}]", record.value());
    }
}
