package org.example.issuecoupon.utils;

import lombok.experimental.UtilityClass;
import org.springframework.web.util.UriComponentsBuilder;

import static java.nio.charset.StandardCharsets.UTF_8;

@UtilityClass
public class RequestUrlUtils {

    public static final String COUPON_SERVICE_URL = "http://localhost:8082";
    public static final String GET_COUPONS_FROM_DB_ENDPOINT = "/coupons/{couponId}";

    public static String buildUriWithPathVariable(String baseUrl, String endpoint, Long pathVariable) {
        return UriComponentsBuilder
                .fromUriString(baseUrl)
                .path(endpoint)
                .buildAndExpand(pathVariable)
                .encode(UTF_8)
                .toUriString();
    }
}
