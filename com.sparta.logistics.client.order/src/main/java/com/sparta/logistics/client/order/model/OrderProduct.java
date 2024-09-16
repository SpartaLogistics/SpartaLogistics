package com.sparta.logistics.client.order.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.sparta.logistics.client.order.dto.OrderProductRequestDto;
import com.sparta.logistics.common.model.Timestamped;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "p_order_products")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderProduct extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "order_product_id", columnDefinition = "UUID")
    private UUID orderProductId;

    @Column(name = "order_id", columnDefinition = "UUID", nullable = false)
    private UUID orderId;

    @Column(name = "product_id", columnDefinition = "UUID", nullable = false)
    private UUID productId;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    @ManyToOne
    @JoinColumn(name = "order_id", insertable = false, updatable = false)
    @JsonBackReference
    private Order order;

    public void softDelete() {
        this.isDeleted = true;
    }

    // order product create builder
    @Builder(builderClassName = "OrderProductCreateBuilder", builderMethodName = "OrderProductCreateBuilder")
    public OrderProduct(UUID orderId, OrderProductRequestDto orderProductRequestDto) {
        this.orderId = orderId;
        this.productId = orderProductRequestDto.getProductId();
        this.quantity = orderProductRequestDto.getQuantity();
        this.isDeleted = false;
    }

    // order producrt update builder
    // 수량만 수정 가능
    @Builder(builderClassName = "OrderProductUpdateBuilder", builderMethodName = "OrderProductUpdateBuilder")
    public OrderProduct(OrderProductRequestDto orderProductRequestDto) {
        this.orderProductId = orderProductRequestDto.getOrderId();
        this.quantity = orderProductRequestDto.getQuantity();
    }
}
