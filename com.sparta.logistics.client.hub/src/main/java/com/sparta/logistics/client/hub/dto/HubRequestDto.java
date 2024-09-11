package com.sparta.logistics.client.hub.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HubRequestDto {

    private String name;          // 허브 이름
    private String address;       // 허브 주소
    private BigDecimal latitude;  // 위도
    private BigDecimal longitude; // 경도
}
