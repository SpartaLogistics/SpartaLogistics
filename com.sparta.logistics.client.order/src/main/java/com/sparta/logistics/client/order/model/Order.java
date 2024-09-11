package com.sparta.logistics.client.order.model;


import com.sparta.logistics.client.order.dto.OrderRequestDto;
import com.sparta.logistics.client.order.common.type.OrderStatus;

import com.sparta.logistics.common.model.Timestamped;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "p_orders")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "order_id", columnDefinition = "UUID")
    private UUID orderId;

    @Column(name = "sender_id", columnDefinition = "UUID", nullable = false)
    private UUID senderId;

    @Column(name = "receiver_id", columnDefinition = "UUID", nullable = false)
    private UUID receiverId;

    @Column(name = "delivery_id", columnDefinition = "UUID")
    private UUID deliveryId;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    private String remark;

    @OneToMany(mappedBy = "order")
    private List<OrderProduct> orderProducts;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status;


    // 소프트 삭제 메서드
    public void softDelete() {
        this.isDeleted = true;
    }


    // order create builder
    @Builder(builderClassName = "OrderCreateBuilder", builderMethodName = "OrderCreateBuilder")
    public Order(OrderRequestDto orderDto) {
        // TODO null check
        this.senderId = orderDto.getSenderId();
        this.receiverId = orderDto.getReceiverId();
        this.status = OrderStatus.ORDER_PENDING;
        this.isDeleted = false;
        this.remark = orderDto.getRemark();
    }

    // order update builder
    @Builder(builderClassName = "OrderUpdateBuilder", builderMethodName = "OrderUpdateBuilder")
    public Order(OrderRequestDto orderDto, Order order) {
        this.senderId = orderDto.getSenderId() != null ? orderDto.getSenderId() : order.getSenderId();
        this.receiverId = orderDto.getReceiverId() != null ? orderDto.getReceiverId() : order.getReceiverId();
        this.deliveryId = order.getDeliveryId() != null ? order.getDeliveryId() : order.getDeliveryId();
        this.remark = order.getRemark() != null ? order.getRemark() : order.getRemark();
        this.status = order.getStatus() != null ? order.getStatus() : order.getStatus();
    }


}
