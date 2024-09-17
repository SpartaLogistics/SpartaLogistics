package com.sparta.logistics.client.order.repository;

import com.sparta.logistics.client.order.model.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderProductRepository extends JpaRepository<OrderProduct, UUID>, OrderProductRepositoryCustom {

    Optional<OrderProduct> findByOrderIdAndIsDeletedFalse(UUID orderId);
    Optional<OrderProduct> findByOrderProductId(UUID orderProductId);
    List<OrderProduct> findByOrderId(UUID orderId);
    List<OrderProduct> findByProductIdAndIsDeletedFalse(UUID productId);
}
