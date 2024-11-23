package org.example.couponkafka.infrastructure;

import org.example.couponkafka.infrastructure.entity.CouponIssueEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponIssueJpaRepository extends JpaRepository<CouponIssueEntity, Long> {
}
