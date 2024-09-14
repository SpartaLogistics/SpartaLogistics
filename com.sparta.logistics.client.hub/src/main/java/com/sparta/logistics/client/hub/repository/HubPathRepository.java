package com.sparta.logistics.client.hub.repository;

import com.sparta.logistics.client.hub.model.HubPath;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface HubPathRepository extends JpaRepository<HubPath, UUID>, HubPathRepositoryCustom {
}
