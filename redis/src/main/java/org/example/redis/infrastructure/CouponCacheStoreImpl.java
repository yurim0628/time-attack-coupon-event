package org.example.redis.infrastructure;

import lombok.RequiredArgsConstructor;
import org.example.redis.domain.CouponCache;
import org.example.redis.infrastructure.vo.CouponRedisVO;
import org.example.redis.service.port.CouponCacheStore;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CouponCacheStoreImpl implements CouponCacheStore {

    private final CouponRedisCache couponRedisCache;

    @Override
    public Optional<CouponCache> getCoupon(String key) {
        return couponRedisCache.getCoupon(key).map(CouponRedisVO::toModel);
    }

    @Override
    public void saveCoupon(String key, CouponCache couponCache) {
        couponRedisCache.setCoupon(key, CouponRedisVO.fromModel(couponCache));
    }
}
