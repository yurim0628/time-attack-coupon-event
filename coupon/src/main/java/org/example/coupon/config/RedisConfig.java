package org.example.coupon.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    private static final String MASTER_NAME = "my-master";
    private static final String SENTINEL_HOST = "localhost";
    private static final int SENTINEL_PORT_1 = 5000;
    private static final int SENTINEL_PORT_2 = 5001;
    private static final int SENTINEL_PORT_3 = 5002;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisSentinelConfiguration redisSentinelConfiguration = new RedisSentinelConfiguration()
                .master(MASTER_NAME)
                .sentinel(SENTINEL_HOST, SENTINEL_PORT_1)
                .sentinel(SENTINEL_HOST, SENTINEL_PORT_2)
                .sentinel(SENTINEL_HOST, SENTINEL_PORT_3);
        return new LettuceConnectionFactory(redisSentinelConfiguration);
    }

    @Bean
    public RedisTemplate<String, String> redisTemplate() {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        return redisTemplate;
    }
}
