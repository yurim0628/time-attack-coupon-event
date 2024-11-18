package org.example.issuecoupon.infrastructure.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.issuecoupon.exception.IssueCouponException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.example.issuecoupon.exception.ErrorCode.COMMON_SYSTEM_ERROR;

@Slf4j
@Component
@RequiredArgsConstructor
public class IssueCouponRestTemplateClient {

    private final RestTemplate restTemplate;

    public <T> Optional<T> get(String url, Class<T> type) {
        try {
            return Optional.ofNullable(restTemplate.getForObject(url, type));
        } catch (HttpClientErrorException e) {
            log.error("RestTemplate Get Exception Message = [{}]", e.getMessage());
            return Optional.empty();
        } catch (Exception e) {
            log.error("RestTemplate Get Exception", e);
            throw new IssueCouponException(COMMON_SYSTEM_ERROR);
        }
    }
}
