package org.example.couponkafka.config;

import org.example.couponkafka.domain.CouponIssue;
import org.example.couponkafka.service.DltMessageHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.retrytopic.RetryTopicConfiguration;
import org.springframework.kafka.retrytopic.RetryTopicConfigurationBuilder;
import org.springframework.kafka.support.EndpointHandlerMethod;

@Configuration
public class KafkaRetryConfig {

    private static final long FIXED_BACKOFF_INTERVAL_MS = 1000L;
    private static final int MAX_RETRY_ATTEMPTS = 3;

    @Bean
    public RetryTopicConfiguration retryTopicConfiguration(KafkaTemplate<String, CouponIssue> kafkaTemplate) {
        return RetryTopicConfigurationBuilder
                .newInstance()
                .fixedBackOff(FIXED_BACKOFF_INTERVAL_MS)
                .maxAttempts(MAX_RETRY_ATTEMPTS)
                .dltHandlerMethod(new EndpointHandlerMethod(
                        DltMessageHandler.class,
                        "handleDltMessage"
                ))
                .create(kafkaTemplate);
    }
}
