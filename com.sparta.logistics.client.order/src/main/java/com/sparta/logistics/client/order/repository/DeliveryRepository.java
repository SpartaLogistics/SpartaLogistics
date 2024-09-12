package com.sparta.logistics.client.order.repository;

import com.sparta.logistics.client.order.model.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeliveryRepository extends JpaRepository<Delivery, UUID> {

    Optional<Delivery> findByOrderIdAndIsDeletedFalse(UUID orderId);
    Optional<Delivery> findByDeliveryIdAndIsDeletedFalse(UUID deliveryId);
    List<Delivery> findAllByIsDeletedFalse();
}
