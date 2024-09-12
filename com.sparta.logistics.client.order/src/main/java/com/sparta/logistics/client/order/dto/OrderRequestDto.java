package com.sparta.logistics.client.order.dto;

import com.sparta.logistics.client.order.common.type.OrderStatus;
import com.sparta.logistics.client.order.model.validation.OrderValid0001;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(groups = {OrderValid0001.class},
            message = "발송처 정보가 누락되었습니다.")
    private UUID senderId;

    @NotNull(groups = {OrderValid0001.class},
            message = "수령처 정보가 누락되었습니다.")
    private UUID receiverId;

    private UUID deliveryId;
    private String remark;
    private OrderStatus status;

    // 주문 품목
    private List<OrderProductRequestDto> orderProducts = new ArrayList<>();


}
