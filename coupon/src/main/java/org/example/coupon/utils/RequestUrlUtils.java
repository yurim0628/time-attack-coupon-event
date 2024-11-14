package org.example.coupon.utils;

import lombok.experimental.UtilityClass;
import org.springframework.web.util.UriComponentsBuilder;

import static java.nio.charset.StandardCharsets.UTF_8;

@UtilityClass
public class RequestUrlUtils {

    public static final String REDIS_SERVICE_URL = "http://localhost:8084";
    public static final String GET_ISSUED_COUPON_COUNT_ENDPOINT = "/redis/coupon-issues/{couponId}/count";;

    public static String buildUriWithPathVariable(String baseUrl, String endpoint, Long pathVariable) {
        return UriComponentsBuilder
                .fromUriString(baseUrl)
                .path(endpoint)
                .buildAndExpand(pathVariable)
                .encode(UTF_8)
                .toUriString();
    }
}
