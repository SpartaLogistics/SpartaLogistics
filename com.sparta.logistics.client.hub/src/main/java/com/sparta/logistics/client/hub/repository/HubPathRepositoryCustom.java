package com.sparta.logistics.client.hub.repository;

import com.sparta.logistics.client.hub.model.Hub;
import com.sparta.logistics.client.hub.model.HubPath;

import java.util.List;

public interface HubPathRepositoryCustom {
    List<HubPath> findPathsBetweenHubs(Hub startHub, Hub endHub);

    List<HubPath> findPathsBetweenHubsReverse(Hub startHub, Hub endHub);
}
