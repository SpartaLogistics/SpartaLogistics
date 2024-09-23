package com.sparta.logistics.client.hub.repository;

import com.sparta.logistics.client.hub.model.Hub;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HubRepository extends JpaRepository<Hub, UUID>, HubRepositoryCustom {
    Optional<Hub> findByHubIdAndIsDeletedFalse(UUID hubId);

    default Optional<Hub> findByHubId(UUID hubId) {
        return findByHubIdAndIsDeletedFalse(hubId);
    }

    Optional<Hub> findByNameAndIsDeletedFalse(String name);

    default Optional<Hub> findByHubName(String name) {
        return findByNameAndIsDeletedFalse(name);
    }

    List<Hub> findAllByIsDeletedFalse();

    boolean existsBySequenceAndIsDeletedFalse(Integer sequence);
}
