package org.example.redis.service.port;

public interface CouponIssueCacheStore {

    Long getIssuedCouponUserCount(String issueRequestKey);

    Long addIssuedCouponUser(String key, String userId);
}
