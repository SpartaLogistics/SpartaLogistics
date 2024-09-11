package com.sparta.logistics.client.order.dto;

import com.sparta.logistics.client.order.common.type.OrderStatus;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDto {

    private UUID orderId;
    private UUID senderId;
    private UUID receiverId;
    private UUID deliveryId;
    private String remark;
    private OrderStatus status;

    // 주문 품목
    private List<OrderProductDto> orderProducts = new ArrayList<>();


}
