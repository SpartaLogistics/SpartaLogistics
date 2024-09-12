package com.sparta.logistics.client.hub.repository;

import com.querydsl.core.BooleanBuilder;
import com.sparta.logistics.client.hub.model.Hub;
import com.sparta.logistics.client.hub.model.QHub;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.util.StringUtils;

import java.util.List;

public class HubRepositoryImpl extends QuerydslRepositorySupport implements HubRepositoryCustom {

    public HubRepositoryImpl() {
        super(Hub.class);
    }
    
    @Override
    public List<Hub> searchHubs(String name, String address) {
        QHub hub = QHub.hub;

        BooleanBuilder builder = new BooleanBuilder();

        if (StringUtils.hasText(name)) {
            builder.and(hub.name.containsIgnoreCase(name));
        }

        if (StringUtils.hasText(address)) {
            builder.and(hub.address.containsIgnoreCase(address));
        }

        return from(hub)
                .where(builder)
                .fetch();

    }
}
