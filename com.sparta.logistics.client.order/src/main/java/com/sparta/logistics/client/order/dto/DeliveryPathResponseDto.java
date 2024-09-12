package com.sparta.logistics.client.order.dto;

import com.sparta.logistics.client.order.model.DeliveryPath;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class DeliveryPathResponseDto {

    private UUID deliveryPathId;
    private UUID deliveryId;
    private Integer sequence;
    private UUID departureId;
    private UUID arrivalId;
    private Long expectedDistance;
    private Long expectedTime;
    private Long actualDistance;
    private Long actualTime;

    public static DeliveryPathResponseDto of(DeliveryPath deliveryPath) {
        return DeliveryPathResponseDto.builder()
                .deliveryPathId(deliveryPath.getDeliveryPathId())
                .deliveryId(deliveryPath.getDelivery().getDeliveryId())
                .sequence(deliveryPath.getSequence())
                .departureId(deliveryPath.getDepartureId())
                .arrivalId(deliveryPath.getArrivalId())
                .expectedDistance(deliveryPath.getExpectedDistance())
                .expectedTime(deliveryPath.getExpectedTime())
                .actualDistance(deliveryPath.getActualDistance())
                .actualTime(deliveryPath.getActualTime())
                .build();
    }
}
