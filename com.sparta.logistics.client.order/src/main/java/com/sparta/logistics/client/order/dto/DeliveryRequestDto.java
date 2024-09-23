package com.sparta.logistics.client.order.dto;

import lombok.*;

import java.util.UUID;

@Data
public class DeliveryRequestDto {

    private UUID orderId;
    private UUID deliveryId;
    private UUID arrivalId;
    private UUID departureId;
    private UUID receiverId;
    private String receiver;
    private String slackId;
    private String address;

}
