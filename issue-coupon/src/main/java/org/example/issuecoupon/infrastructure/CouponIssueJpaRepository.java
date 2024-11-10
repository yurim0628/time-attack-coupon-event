package org.example.issuecoupon.infrastructure;

import org.example.issuecoupon.infrastructure.entity.CouponIssueEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponIssueJpaRepository extends JpaRepository<CouponIssueEntity, Long> {

    boolean existsByCouponIdAndUserId(Long couponId, Long userId);
}
