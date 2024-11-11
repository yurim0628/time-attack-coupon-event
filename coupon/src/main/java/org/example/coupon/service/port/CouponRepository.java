package org.example.coupon.service.port;

import org.example.coupon.domain.Coupon;

import java.util.List;
import java.util.Optional;

public interface CouponRepository {

    Optional<Coupon> findById(Long id);

    void updateIssuedQuantity(Long id, Long issuedCount);

    List<Coupon> findAll();
}
