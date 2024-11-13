package org.example.issuecoupon.infrastructure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.issuecoupon.exception.IssueCouponException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.example.issuecoupon.exception.ErrorCode.COMMON_JSON_PROCESSING_ERROR;
import static org.example.issuecoupon.exception.ErrorCode.COMMON_SYSTEM_ERROR;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
@Component
@RequiredArgsConstructor
public class IssueCouponRestTemplateClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

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

    public <T, R> String post(String url, R request, Class<T> type) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON);
        HttpEntity<R> entity = new HttpEntity<>(request, headers);
        try {
            restTemplate.postForObject(url, entity, type);
        } catch (HttpClientErrorException e) {
            log.error("RestTemplate Post Exception Message = [{}]", e.getMessage());
            try {
                JsonNode responseBody = objectMapper.readTree(e.getResponseBodyAsString());
                return responseBody.get("code").asText();
            } catch (JsonProcessingException je) {
                log.error("Error Parsing JSON Response Body", je);
                throw new IssueCouponException(COMMON_JSON_PROCESSING_ERROR);
            }
        } catch (Exception e) {
            log.error("RestTemplate Post Exception", e);
            throw new IssueCouponException(COMMON_SYSTEM_ERROR);
        }
        return null;
    }
}
