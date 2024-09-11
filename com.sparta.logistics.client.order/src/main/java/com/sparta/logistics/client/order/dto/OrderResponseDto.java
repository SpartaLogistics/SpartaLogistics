package com.sparta.logistics.client.order.dto;

import com.sparta.logistics.client.order.common.type.OrderStatus;
import com.sparta.logistics.client.order.model.Order;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class OrderResponseDto {

    private UUID senderId;
    private UUID receiverId;
    private UUID deliveryId;
    private OrderStatus status;
    private boolean isDeleted;
    private String remark;

    private List<OrderProductDto> products = new ArrayList<>();

    //private String senderNm;        // 발송처
    //private String receiverNm;      // 도착처

    public OrderResponseDto(Order order) {
        this.senderId = order.getSenderId();
        this.receiverId = order.getReceiverId();
        this.deliveryId = order.getDeliveryId();
        this.status = order.getStatus();
        this.isDeleted = order.isDeleted();
        this.remark = order.getRemark();
    }
}
