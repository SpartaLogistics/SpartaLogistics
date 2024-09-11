package com.sparta.logistics.client.hub.dto;


import com.sparta.logistics.client.hub.model.Hub;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HubResponseDto {

    private UUID hubId;           // 허브 ID
    private String name;          // 허브 이름
    private String address;       // 허브 주소
    private BigDecimal latitude;  // 위도
    private BigDecimal longitude; // 경도
    private Boolean is_deleted;   // 삭제 여부

    public static HubResponseDto of(Hub hub) {
        return HubResponseDto.builder()
                .hubId(hub.getHubId())
                .name(hub.getName())
                .address(hub.getAddress())
                .latitude(hub.getLatitude())
                .longitude(hub.getLongitude())
                .is_deleted(hub.getIs_deleted())
                .build();
    }
}
