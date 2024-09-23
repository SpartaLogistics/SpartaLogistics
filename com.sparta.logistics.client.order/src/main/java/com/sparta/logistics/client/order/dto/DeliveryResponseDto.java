package com.sparta.logistics.client.order.dto;

import com.sparta.logistics.client.order.common.type.DeliveryStatus;
import com.sparta.logistics.client.order.model.Delivery;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryResponseDto {

    private UUID deliveryId;
    private UUID orderId;
    private UUID arrivalID;
    private UUID departureId;
    private DeliveryStatus status;
    private String receiver;
    private String address;
    private String slackId;

    public static DeliveryResponseDto of(Delivery delivery) {
        return DeliveryResponseDto.builder()
                .deliveryId(delivery.getDeliveryId())
                .orderId(delivery.getOrderId())
                .arrivalID(delivery.getArrivalId())
                .departureId(delivery.getDepartureId())
                .status(delivery.getStatus())
                .receiver(delivery.getReceiver())
                .address(delivery.getAddress())
                .slackId(delivery.getSlackId())
                .build();
    }

}
