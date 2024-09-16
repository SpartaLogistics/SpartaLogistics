package com.sparta.logistics.client.order.client.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class HubPathResponseDto {

    private UUID hubPathId; // 허브 이동 경로 ID
    private UUID hubId; // 허브 ID
    private String hubName; // 허브명
    private String address; // 주소
    private UUID nextHubId; // 다음 허브 ID
    private String nextHubName;
    private String nextHubAddress;

    private Long duration; // 소요 시간
}
