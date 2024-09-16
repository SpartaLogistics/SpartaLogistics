package com.sparta.logistics.client.order.dto;

import com.sparta.logistics.client.order.model.OrderProduct;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class OrderProductResponseDto {

    private UUID orderId;
    private UUID orderProductId;
    private UUID productId;
    private int quantity;

    public static OrderProductResponseDto of(OrderProduct orderProduct) {
        return OrderProductResponseDto.builder()
                .orderId(orderProduct.getOrderId())
                .orderProductId(orderProduct.getOrderProductId())
                .productId(orderProduct.getProductId())
                .quantity(orderProduct.getQuantity())
                .build();
    }
}
