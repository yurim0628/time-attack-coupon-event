package org.example.coupon.infrastructure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.coupon.exception.CouponException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.example.coupon.exception.ErrorCode.COMMON_SYSTEM_ERROR;

@Slf4j
@Component
@RequiredArgsConstructor
public class CouponRestTemplateClient {

    private final RestTemplate restTemplate;

    public <T> Optional<T> get(String url, Class<T> type) {
        try {
            return Optional.ofNullable(restTemplate.getForObject(url, type));
        } catch (HttpClientErrorException e) {
            log.error("RestTemplate Get Exception Message = [{}]", e.getMessage());
            return Optional.empty();
        } catch (Exception e) {
            log.error("RestTemplate Get Exception", e);
            throw new CouponException(COMMON_SYSTEM_ERROR);
        }
    }
}
