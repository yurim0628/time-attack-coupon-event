package org.example.coupon.service.port;

import org.example.coupon.domain.Coupon;

import java.util.Optional;

public interface CouponRepository {

    Optional<Coupon> findById(Long id);

    void save(Coupon coupon);
}
