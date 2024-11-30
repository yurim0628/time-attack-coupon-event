package org.example.couponkafka.config;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.example.couponkafka.domain.CouponIssue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

import static org.apache.kafka.clients.consumer.ConsumerConfig.*;
import static org.springframework.kafka.listener.ContainerProperties.AckMode.MANUAL_IMMEDIATE;
import static org.springframework.kafka.support.serializer.ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS;
import static org.springframework.kafka.support.serializer.ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS;
import static org.springframework.kafka.support.serializer.JsonDeserializer.TRUSTED_PACKAGES;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id}")
    private String consumerGroupId;

    @Value("${spring.kafka.consumer.trusted-packages}")
    private String trustedPackages;

    @Value("${spring.kafka.consumer.enable-auto-commit}")
    private boolean enableAutoCommit;

    @Value("${spring.kafka.consumer.auto-offset-reset}")
    private String autoOffsetReset;

    @Bean
    public DefaultKafkaConsumerFactory<String, CouponIssue> consumerFactory() {
        Map<String, Object> consumerConfig = new HashMap<>();
        consumerConfig.put(BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        consumerConfig.put(GROUP_ID_CONFIG, consumerGroupId);

        consumerConfig.put(KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        consumerConfig.put(VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        consumerConfig.put(VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);
        consumerConfig.put(KEY_DESERIALIZER_CLASS, StringDeserializer.class);

        consumerConfig.put(TRUSTED_PACKAGES, trustedPackages);

        consumerConfig.put(ENABLE_AUTO_COMMIT_CONFIG, enableAutoCommit);
        consumerConfig.put(AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);

        return new DefaultKafkaConsumerFactory<>(
                consumerConfig,
                new StringDeserializer(),
                new JsonDeserializer<>(CouponIssue.class, false)
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, CouponIssue> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, CouponIssue> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.getContainerProperties().setAckMode(MANUAL_IMMEDIATE);
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}
