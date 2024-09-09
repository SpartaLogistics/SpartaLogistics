package com.sparta.logistics.client.order.domain.model;

import com.sparta.logistics.client.order.common.Timestamped;
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
public class OrderProducts extends Timestamped {

    @Id
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
    private Orders order;
}
