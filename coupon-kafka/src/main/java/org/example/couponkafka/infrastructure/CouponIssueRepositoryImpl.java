package org.example.couponkafka.infrastructure;

import lombok.RequiredArgsConstructor;
import org.example.couponkafka.domain.CouponIssue;
import org.example.couponkafka.infrastructure.entity.CouponIssueEntity;
import org.example.couponkafka.service.port.CouponIssueRepository;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CouponIssueRepositoryImpl implements CouponIssueRepository {

    private final CouponIssueJpaRepository couponIssueJpaRepository;

    @Override
    public CouponIssue save(CouponIssue couponIssue) {
        return couponIssueJpaRepository.save(CouponIssueEntity.fromModel(couponIssue)).toModel();
    }
}
