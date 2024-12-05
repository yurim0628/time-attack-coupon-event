package org.example.couponkafka.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.example.couponkafka.domain.CouponIssue;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import static org.springframework.kafka.support.KafkaHeaders.RECEIVED_TOPIC;

@Service
@Slf4j
public class DltMessageHandler {

    private static final String ORIGINAL_TOPIC = "TimeAttackCouponIssue";
    private final KafkaTemplate<String, CouponIssue> kafkaTemplate;

    public DltMessageHandler(KafkaTemplate<String, CouponIssue> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void handleDltMessage(ConsumerRecord<String, CouponIssue> record,
                                 @Header(RECEIVED_TOPIC) String topic) {
        log.error("Received Message in DLT. Topic: [{}], Value: [{}]", topic, record.value());
        kafkaTemplate.send(ORIGINAL_TOPIC, record.key(), record.value());
        log.info("Message Successfully Reprocessed. Value: [{}]", record.value());
    }
}
