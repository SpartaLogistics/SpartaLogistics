package com.sparta.logistics.client.hub.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.logistics.client.hub.model.Hub;
import com.sparta.logistics.client.hub.model.HubPath;
import com.sparta.logistics.client.hub.model.QHub;
import com.sparta.logistics.client.hub.model.QHubPath;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class HubPathRepositoryImpl extends QuerydslRepositorySupport implements HubPathRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public HubPathRepositoryImpl(JPAQueryFactory queryFactory) {
        super(HubPath.class);
        this.queryFactory = queryFactory;
    }


    @Override
    public List<HubPath> findPathsBetweenHubs(Hub startHub, Hub endHub) {
        QHubPath qHubPath = QHubPath.hubPath;
        QHub qDepartureHub = new QHub("departureHub");
        QHub qArrivalHub = new QHub("arrivalHub");

        return from(qHubPath)
                .join(qHubPath.departureHub, qDepartureHub).fetchJoin()
                .join(qHubPath.arrivalHub, qArrivalHub).fetchJoin()
                .where(qDepartureHub.sequence.between(startHub.getSequence(), endHub.getSequence() - 1)
                        .and(qHubPath.isDeleted.eq(false)))
                .orderBy(qDepartureHub.sequence.asc())
                .fetch();
    }

    @Override
    public List<HubPath> findPathsBetweenHubsReverse(Hub startHub, Hub endHub) {
        QHubPath qHubPath = QHubPath.hubPath;
        QHub qDepartureHub = new QHub("departureHub");
        QHub qArrivalHub = new QHub("arrivalHub");

        return from(qHubPath)
                .join(qHubPath.departureHub, qDepartureHub).fetchJoin()
                .join(qHubPath.arrivalHub, qArrivalHub).fetchJoin()
                .where(qDepartureHub.sequence.between(endHub.getSequence(), startHub.getSequence() - 1)
                        .and(qHubPath.isDeleted.eq(false)))
                .orderBy(qDepartureHub.sequence.asc())
                .fetch();
    }


    @Override
    public Optional<HubPath> findByHubPathIdWithHubs(UUID hubPathId) {
        QHubPath hubPath = QHubPath.hubPath;
        return Optional.ofNullable(queryFactory
                .selectFrom(hubPath)
                .join(hubPath.departureHub).fetchJoin()
                .join(hubPath.arrivalHub).fetchJoin()
                .where(hubPath.hubPathId.eq(hubPathId)
                        .and(hubPath.isDeleted.isFalse()))
                .fetchOne());
    }

    @Override
    public List<HubPath> findAllByIsDeletedFalse() {
        QHubPath qHubPath = QHubPath.hubPath;
        QHub qDepartureHub = QHub.hub;
        QHub qArrivalHub = new QHub("arrivalHub");

        return queryFactory
                .selectFrom(qHubPath)
                .join(qHubPath.departureHub, qDepartureHub).fetchJoin()
                .join(qHubPath.arrivalHub, qArrivalHub).fetchJoin()
                .where(qHubPath.isDeleted.isFalse())
                .fetch();
    }


    @Override
    public Page<HubPath> searchPaths(UUID departureHubId, UUID arrivalHubId, Long minDuration, Long maxDuration, Pageable pageable) {
        QHubPath hubPath = QHubPath.hubPath;
        QHub departureHub = QHub.hub;
        QHub arrivalHub = new QHub("arrivalHub");

        List<HubPath> content = queryFactory
                .selectFrom(hubPath)
                .join(hubPath.departureHub, departureHub).fetchJoin()
                .join(hubPath.arrivalHub, arrivalHub).fetchJoin()
                .where(
                        departureHubIdEq(departureHubId),
                        arrivalHubIdEq(arrivalHubId),
                        durationBetween(minDuration, maxDuration),
                        hubPath.isDeleted.isFalse()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .selectFrom(hubPath)
                .where(
                        departureHubIdEq(departureHubId),
                        arrivalHubIdEq(arrivalHubId),
                        durationBetween(minDuration, maxDuration),
                        hubPath.isDeleted.isFalse()
                )
                .fetchCount();

        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression departureHubIdEq(UUID departureHubId) {
        return departureHubId != null ? QHubPath.hubPath.departureHub.hubId.eq(departureHubId) : null;
    }

    private BooleanExpression arrivalHubIdEq(UUID arrivalHubId) {
        return arrivalHubId != null ? QHubPath.hubPath.arrivalHub.hubId.eq(arrivalHubId) : null;
    }

    private BooleanExpression durationBetween(Long minDuration, Long maxDuration) {
        if (minDuration != null && maxDuration != null) {
            return QHubPath.hubPath.duration.between(minDuration, maxDuration);
        } else if (minDuration != null) {
            return QHubPath.hubPath.duration.goe(minDuration);
        } else if (maxDuration != null) {
            return QHubPath.hubPath.duration.loe(maxDuration);
        }
        return null;
    }
}
