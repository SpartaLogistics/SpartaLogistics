package com.sparta.logistics.client.order.domain.model;

import com.sparta.logistics.client.order.common.Timestamped;
import com.sparta.logistics.client.order.common.type.OrderStatus;
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
public class Orders extends Timestamped {

    @Id
    @Column(name = "order_id", columnDefinition = "UUID")
    private UUID orderId;

    @Column(name = "sender_id", columnDefinition = "UUID", nullable = false)
    private UUID senderId;

    @Column(name = "receiver_id", columnDefinition = "UUID", nullable = false)
    private UUID receiverId;

    @Column(name = "delivery_id", columnDefinition = "UUID", nullable = false)
    private UUID deliveryId;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    private String remark;

    @OneToMany(mappedBy = "order")
    private List<OrderProducts> orderProducts;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status;

}
