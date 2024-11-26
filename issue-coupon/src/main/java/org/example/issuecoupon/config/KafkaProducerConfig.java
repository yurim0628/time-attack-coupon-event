package org.example.issuecoupon.config;

import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
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

    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        Map<String, Object> producerConfig = new HashMap<>();
        producerConfig.put(BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        producerConfig.put(KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerConfig.put(VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        producerConfig.put(BATCH_SIZE_CONFIG, batchSize);
        producerConfig.put(LINGER_MS_CONFIG, lingerMs);
        producerConfig.put(BUFFER_MEMORY_CONFIG, bufferMemory);

        return new DefaultKafkaProducerFactory<>(producerConfig);
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
