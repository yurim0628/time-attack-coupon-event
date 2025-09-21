package org.example.issuecoupon.config;

import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.micrometer.KafkaTemplateObservation;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

import static org.apache.kafka.clients.producer.ProducerConfig.*;

@Configuration
public class KafkaProducerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.producer.batch-size}")
    private int batchSize;

    @Value("${spring.kafka.producer.linger-ms}")
    private int lingerMs;

    @Value("${spring.kafka.producer.buffer-memory}")
    private int bufferMemory;

    @Value("${spring.kafka.producer.compression-type}")
    private String compressionType;

    @Value("${spring.kafka.producer.acks}")
    private String acks;

    @Value("${spring.kafka.producer.properties.enable.idempotence}")
    private boolean enableIdempotence;

    @Value("${spring.kafka.producer.retries}")
    private int retries;

    @Value("${spring.kafka.producer.properties.max.in.flight.requests.per.connection}")
    private int maxInflightRequests;

    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        Map<String, Object> producerConfig = new HashMap<>();

        // Messaging behavior-related configurations
        producerConfig.put(BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        producerConfig.put(KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerConfig.put(VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        // Batch and compression-related configurations
        producerConfig.put(BATCH_SIZE_CONFIG, batchSize);
        producerConfig.put(LINGER_MS_CONFIG, lingerMs);
        producerConfig.put(BUFFER_MEMORY_CONFIG, bufferMemory);
        producerConfig.put(COMPRESSION_TYPE_CONFIG, compressionType);

        // Reliability-related configurations
        producerConfig.put(ACKS_CONFIG, acks);
        producerConfig.put(RETRIES_CONFIG, retries);
        producerConfig.put(ENABLE_IDEMPOTENCE_CONFIG, enableIdempotence);
        producerConfig.put(MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, maxInflightRequests);

        return new DefaultKafkaProducerFactory<>(producerConfig);
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        KafkaTemplate<String, Object> kafkaTemplate = new KafkaTemplate<>(producerFactory());
        kafkaTemplate.setObservationEnabled(true);
        kafkaTemplate.setObservationConvention(
                KafkaTemplateObservation.DefaultKafkaTemplateObservationConvention.INSTANCE
        );
        return kafkaTemplate;
    }
}
