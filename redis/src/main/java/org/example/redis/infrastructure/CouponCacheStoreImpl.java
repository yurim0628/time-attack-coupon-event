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
    public void saveCoupon(String saveCouponKey, CouponCache couponCache) {
        couponRedisCache.setCoupon(saveCouponKey, CouponRedisVO.fromModel(couponCache));
    }

    @Override
    public Optional<CouponCache> getCoupon(String getCouponKey) {
        return couponRedisCache.getCoupon(getCouponKey)
                .map(CouponRedisVO::toModel)
                .or(Optional::empty);
    }
}
