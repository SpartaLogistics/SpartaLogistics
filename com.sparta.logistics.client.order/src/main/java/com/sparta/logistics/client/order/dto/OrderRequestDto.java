package com.sparta.logistics.client.order.dto;

import com.sparta.logistics.client.order.common.type.OrderStatus;
import com.sparta.logistics.client.order.model.validation.OrderValid0001;
import com.sparta.logistics.client.order.model.validation.OrderValid0002;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class OrderRequestDto {

    // 주문정보
    @NotNull(groups = {OrderValid0002.class},
            message = "주문 ID가 누락되었습니다.")
    private UUID orderId;

    @NotNull(groups = {OrderValid0001.class},
            message = "발송처 정보가 누락되었습니다.")
    private UUID senderId;

    @NotNull(groups = {OrderValid0001.class},
            message = "수령처 정보가 누락되었습니다.")
    private UUID receiverId;

    private UUID deliveryId;
    private String remark;
    private OrderStatus status;

    // 배송 정보
    @Valid
    @NotNull(groups = {OrderValid0001.class},
            message = "배송 정보가 누락되었습니다,")
    private DeliveryRequestDto deliveryInfo;

    // 주문 품목
    @Size(min = 1,
        groups = {OrderValid0001.class},
        message = "주문 품목은 1개 이상이어야 합니다.")
    private List<OrderProductRequestDto> orderProducts = new ArrayList<>();


}
