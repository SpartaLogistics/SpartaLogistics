package com.sparta.logistics.client.hub.repository;

import com.sparta.logistics.client.hub.model.Hub;
import com.sparta.logistics.client.hub.model.HubPath;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface HubPathRepositoryCustom {
    List<HubPath> findPathsBetweenHubs(Hub startHub, Hub endHub);

    List<HubPath> findPathsBetweenHubsReverse(Hub startHub, Hub endHub);

    Page<HubPath> searchPaths(UUID departureHubId, UUID arrivalHubId, Long minDuration, Long maxDuration, Pageable pageable);
}
