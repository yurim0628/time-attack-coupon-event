package org.example.coupon.infrastructure.business;

import org.example.coupon.infrastructure.business.entity.CouponEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CouponJpaRepository extends JpaRepository<CouponEntity, Long> {

    @Modifying
    @Query("""
           UPDATE CouponEntity c
           SET c.issuedQuantity = :issuedQuantity
           WHERE c.id = :id
           """)
    void updateIssuedQuantity(
            @Param("id") Long id,
            @Param("issuedQuantity") Long issuedQuantity
    );
}
