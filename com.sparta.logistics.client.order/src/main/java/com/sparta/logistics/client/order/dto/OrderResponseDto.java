package com.sparta.logistics.client.order.dto;

import com.sparta.logistics.client.order.common.type.OrderStatus;
import com.sparta.logistics.client.order.model.Delivery;
import com.sparta.logistics.client.order.model.DeliveryPath;
import com.sparta.logistics.client.order.model.Order;
import com.sparta.logistics.client.order.model.OrderProduct;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDto {

    private UUID orderId;
    private UUID senderId;
    private UUID receiverId;
    private String senderName;
    private String receiverName;
    private UUID deliveryId;
    private OrderStatus status;
    private String statusName;
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
                .status(order.getStatus())
                .isDeleted(order.isDeleted())
                .remark(order.getRemark())
                .build();
    }

    public static OrderResponseDto of(Order order, List<OrderProduct> orderProducts,
                                      Delivery delivery, List<DeliveryPath> deliveryPaths) {
        return OrderResponseDto.builder()
                .orderId(order.getOrderId())
                .senderId(order.getSenderId())
                .receiverId(order.getReceiverId())
                .delivery(DeliveryResponseDto.of(delivery))
                .products(orderProducts.stream()
                        .map(OrderProductResponseDto::of)
                        .collect(Collectors.toList()))
                .deliveryPaths(deliveryPaths.stream()
                        .map(DeliveryPathResponseDto::of)
                        .collect(Collectors.toList()))
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
