package org.example.issuecoupon.infrastructure.cache;

import lombok.RequiredArgsConstructor;
import org.example.issuecoupon.domain.CouponCache;
import org.example.issuecoupon.infrastructure.cache.vo.CouponRedisVO;
import org.example.issuecoupon.service.cache.cache.CouponCacheStore;
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
