package com.sparta.logistics.client.order.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
public class OrderProductRequestDto {

    private UUID orderId;
    private UUID orderProductId;
    private UUID productId;
    private int quantity;
}
