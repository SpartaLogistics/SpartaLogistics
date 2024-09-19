package com.sparta.logistics.client.order.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
public class OrderAIResponseDto {

    private UUID orderId;
    private UUID aiId;
    private String overview;
}
