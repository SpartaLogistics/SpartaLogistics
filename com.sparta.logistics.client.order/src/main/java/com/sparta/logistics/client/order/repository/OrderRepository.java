package com.sparta.logistics.client.order.repository;

import com.sparta.logistics.client.order.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID>, OrderRepositoryCustom {

    Optional<Order> findByOrderIdAndIsDeletedFalse(UUID orderId);
    Page<Order> findAllByIsDeletedFalse(Pageable pageable);
    //Page<Order> findByCreatedByIsDeletedFalse(Pageable pageable);
}
