package com.sparta.logistics.client.order.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Data
public class DeliveryRequestDto {

    // TODO : validation

    private UUID orderId;
    private UUID deliveryId;
    private UUID arrivalId;
    private UUID departureId;
    private UUID receiverId;
    private String receiver;
    private String slackId;
    private String address;

}
