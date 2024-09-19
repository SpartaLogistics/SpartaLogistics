package com.sparta.logistics.client.hub.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.logistics.client.hub.model.Hub;
import com.sparta.logistics.client.hub.model.HubPath;
import com.sparta.logistics.client.hub.model.QHub;
import com.sparta.logistics.client.hub.model.QHubPath;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(hubPath.isDeleted.isFalse());

        if (departureHubId != null) builder.and(hubPath.departureHub.hubId.eq(departureHubId));
        if (arrivalHubId != null) builder.and(hubPath.arrivalHub.hubId.eq(arrivalHubId));
        if (minDuration != null) builder.and(hubPath.duration.goe(minDuration));
        if (maxDuration != null) builder.and(hubPath.duration.loe(maxDuration));

        JPAQuery<HubPath> query = queryFactory
                .selectFrom(hubPath)
                .join(hubPath.departureHub).fetchJoin()
                .join(hubPath.arrivalHub).fetchJoin()
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        for (Sort.Order order : pageable.getSort()) {
            PathBuilder<HubPath> pathBuilder = new PathBuilder<>(HubPath.class, "hubPath");
            if (order.getProperty().equals("duration")) {
                query.orderBy(new OrderSpecifier<>(order.isAscending() ? Order.ASC : Order.DESC,
                        pathBuilder.getNumber(order.getProperty(), Long.class)));
            } else {
                query.orderBy(new OrderSpecifier<>(order.isAscending() ? Order.ASC : Order.DESC,
                        pathBuilder.getString(order.getProperty())));
            }
        }

        List<HubPath> content = query.fetch();
        long total = query.fetchCount();

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
