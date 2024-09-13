package com.sparta.logistics.client.hub.repository;

import com.sparta.logistics.client.hub.model.Hub;
import com.sparta.logistics.client.hub.model.HubPath;
import com.sparta.logistics.client.hub.model.QHub;
import com.sparta.logistics.client.hub.model.QHubPath;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class HubPathRepositoryImpl extends QuerydslRepositorySupport implements HubPathRepositoryCustom {

    public HubPathRepositoryImpl() {
        super(HubPath.class);
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
}
