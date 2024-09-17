package com.sparta.logistics.client.order.dto;

import com.sparta.logistics.client.order.common.type.OrderStatus;
import lombok.Data;

import java.util.UUID;

@Data
public class OrderSearchCriteria {

    private UUID senderId; // 발신자 ID
    private UUID receiverId; // 수신자 ID
    private OrderStatus status; // 주문 상태
    private Boolean isDeleted; // 삭제 여부
}
