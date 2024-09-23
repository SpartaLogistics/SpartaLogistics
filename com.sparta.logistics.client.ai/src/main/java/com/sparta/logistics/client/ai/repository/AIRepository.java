package com.sparta.logistics.client.ai.repository;

import com.sparta.logistics.client.ai.model.AI;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AIRepository extends JpaRepository<AI, UUID>, AIRepositoryCustom {

    Optional<AI> findByAiIdAndIsDeletedFalse(UUID aiId);

}
