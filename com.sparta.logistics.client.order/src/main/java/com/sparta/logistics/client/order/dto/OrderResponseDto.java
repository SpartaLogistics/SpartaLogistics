package com.sparta.logistics.client.order.dto;

import com.sparta.logistics.client.order.common.type.OrderStatus;
import com.sparta.logistics.client.order.model.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    private List<DeliveryPathResponseDto> deliveryPaths = new ArrayList<>();


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

    // 필요한 필드만 포함된 생성자 추가
    public OrderResponseDto(UUID orderId, UUID senderId, UUID receiverId, UUID deliveryId,
                            OrderStatus status, boolean isDeleted, String remark) {
        this.orderId = orderId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.deliveryId = deliveryId;
        this.status = status;
        this.isDeleted = isDeleted;
        this.remark = remark;
    }
}
