package com.sparta.logistics.client.order.client.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class HubResponseDto {

    private UUID hubId;           // 허브 ID
    private String name;          // 허브 이름
    private String address;       // 허브 주소
    private BigDecimal latitude;  // 위도
    private BigDecimal longitude; // 경도
    private boolean is_deleted;   //
}
