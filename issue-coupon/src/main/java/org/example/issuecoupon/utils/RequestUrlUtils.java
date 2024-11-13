package org.example.issuecoupon.utils;

import lombok.experimental.UtilityClass;
import org.springframework.web.util.UriComponentsBuilder;

import static java.nio.charset.StandardCharsets.UTF_8;

@UtilityClass
public class RequestUrlUtils {

    public static final String COUPON_SERVICE_URL = "http://localhost:8082";
    public static final String REDIS_SERVICE_URL = "http://localhost:8084";

    public static final String GET_COUPONS_FROM_DB_ENDPOINT = "/coupons/{couponId}";
    public static final String GET_COUPONS_FROM_CACHE_ENDPOINT = "/redis/coupons/{couponId}";
    public static final String SAVE_COUPONS_FROM_CACHE_ENDPOINT = "/redis/coupons";
    public static final String VALIDATE_ISSUE_COUPONS_ENDPOINT = "/redis/coupon-issues/validate";

    public static String buildUri(String baseUrl, String endpoint) {
        return UriComponentsBuilder
                .fromUriString(baseUrl)
                .path(endpoint)
                .encode(UTF_8)
                .toUriString();
    }

    public static String buildUriWithPathVariable(String baseUrl, String endpoint, Long pathVariable) {
        return UriComponentsBuilder
                .fromUriString(baseUrl)
                .path(endpoint)
                .buildAndExpand(pathVariable)
                .encode(UTF_8)
                .toUriString();
    }
}
