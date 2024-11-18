package org.example.issuecoupon.infrastructure.business;

import org.example.issuecoupon.infrastructure.business.entity.CouponIssueEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponIssueJpaRepository extends JpaRepository<CouponIssueEntity, Long> {
}
