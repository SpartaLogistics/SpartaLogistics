package com.sparta.logistics.client.order.dto;

import com.sparta.logistics.client.order.common.type.OrderStatus;
import com.sparta.logistics.client.order.model.Delivery;
import com.sparta.logistics.client.order.model.Order;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class OrderResponseDto {

    private UUID orderId;
    private UUID senderId;
    private UUID receiverId;
    private UUID deliveryId;
    private OrderStatus status;
    private boolean isDeleted;
    private String remark;

    private DeliveryResponseDto delivery;
    private List<OrderProductResponseDto> products = new ArrayList<>();

    //private String senderNm;        // 발송처
    //private String receiverNm;      // 도착처

    public static OrderResponseDto of(Order order) {
        return OrderResponseDto.builder()
                .orderId(order.getOrderId())
                .senderId(order.getSenderId())
                .receiverId(order.getReceiverId())
                .deliveryId(order.getDeliveryId())
                .status(order.getStatus())
                .isDeleted(order.isDeleted())
                .remark(order.getRemark())
                .build();
    }
}
