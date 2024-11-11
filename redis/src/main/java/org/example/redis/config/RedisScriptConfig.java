package org.example.redis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.script.DefaultRedisScript;

@Configuration
public class RedisScriptConfig {

    private static final String COUPON_USER_COUNT_AND_ADD_SCRIPT = """
        local max_quantity = tonumber(ARGV[1])
        local user_id = ARGV[2]
        local issued_count = redis.call('SCARD', KEYS[1])
        if issued_count >= max_quantity then
            return 1
        end
        if redis.call('SISMEMBER', KEYS[1], user_id) == 1 then
            return 2
        end
        redis.call('SADD', KEYS[1], user_id)
        return 0
        """;

    @Bean
    public DefaultRedisScript<Long> couponUserCountAndAddScript() {
        return createRedisScript(COUPON_USER_COUNT_AND_ADD_SCRIPT);
    }

    private DefaultRedisScript<Long> createRedisScript(String script) {
        return new DefaultRedisScript<>(script, Long.class);
    }
}
