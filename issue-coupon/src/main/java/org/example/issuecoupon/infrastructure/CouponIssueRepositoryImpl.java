package org.example.issuecoupon.infrastructure;

import lombok.RequiredArgsConstructor;
import org.example.issuecoupon.domain.CouponIssue;
import org.example.issuecoupon.infrastructure.entity.CouponIssueEntity;
import org.example.issuecoupon.service.port.CouponIssueRepository;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CouponIssueRepositoryImpl implements CouponIssueRepository {

    private final CouponIssueJpaRepository couponIssueJpaRepository;

    @Override
    public void save(CouponIssue couponIssue) {
        couponIssueJpaRepository.save(CouponIssueEntity.fromModel(couponIssue));
    }
}
