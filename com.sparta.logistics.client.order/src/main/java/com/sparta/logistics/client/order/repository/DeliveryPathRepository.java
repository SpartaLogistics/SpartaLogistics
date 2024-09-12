package com.sparta.logistics.client.order.repository;

import com.sparta.logistics.client.order.model.DeliveryPath;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeliveryPathRepository extends JpaRepository<DeliveryPath, UUID> {

    Optional<DeliveryPath> findByDeliveryPathIdAndIsDeletedFalse(UUID deliveryPathId);
    Optional<DeliveryPath> findAllByIsDeletedFalse();
    List<DeliveryPath> findAllByDeliveryIdAndIsDeletedFalse(UUID deliveryId);

}
