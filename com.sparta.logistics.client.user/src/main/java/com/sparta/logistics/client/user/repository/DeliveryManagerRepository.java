package com.sparta.logistics.client.user.repository;

import com.sparta.logistics.client.user.model.DeliveryManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DeliveryManagerRepository extends JpaRepository<DeliveryManager, UUID> {
    Optional<DeliveryManager> findByIdAndIsDeletedFalse(UUID deliveryId);

}
