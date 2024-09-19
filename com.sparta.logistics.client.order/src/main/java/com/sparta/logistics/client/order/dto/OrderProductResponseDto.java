package com.sparta.logistics.client.order.dto;

import com.sparta.logistics.client.order.model.OrderProduct;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderProductResponseDto {

    private UUID orderId;
    private UUID orderProductId;
    private UUID productId;
    private String productName;
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
