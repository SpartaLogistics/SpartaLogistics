package com.sparta.logistics.client.order.dto;

import com.sparta.logistics.client.order.common.type.DeliveryStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.util.UUID;

@Data
public class DeliveryPathRequestDto {

    private UUID deliveryPathId;
    private UUID deliveryId;
    private Integer sequence;
    private UUID departureId;
    private UUID arrivalId;
    private Long expectedDistance;
    private Long expectedTime;
    private Long actualDistance;
    private Long actualTime;

}
