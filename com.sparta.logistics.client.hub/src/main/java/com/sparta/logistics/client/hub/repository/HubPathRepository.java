package com.sparta.logistics.client.hub.repository;

import com.sparta.logistics.client.hub.model.HubPath;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface HubPathRepository extends JpaRepository<HubPath, UUID>, HubPathRepositoryCustom {

    // 허브 이동 경로 ID 조회
    Optional<HubPath> findByHubPathIdAndIsDeletedFalse(UUID hubPathId);

    default Optional<HubPath> findByHubPathId(UUID hubPathId) {
        return findByHubPathIdAndIsDeletedFalse(hubPathId);
    }

}
