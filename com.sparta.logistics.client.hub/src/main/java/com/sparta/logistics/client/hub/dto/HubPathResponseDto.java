package com.sparta.logistics.client.hub.dto;

import com.sparta.logistics.client.hub.model.Hub;
import com.sparta.logistics.client.hub.model.HubPath;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HubPathResponseDto {

    private UUID hubPathId; // 허브 이동 경로 ID
    private UUID hubId; // 허브 ID
    private String hubName; // 허브명
    private String address; // 주소
    private UUID nextHubId; // 다음 허브 ID
    private String nextHubName;
    private String nextHubAddress;

    private Long duration; // 소요 시간


    public static HubPathResponseDto of(HubPath hubPath) {
        return HubPathResponseDto.builder()
                .hubPathId(hubPath.getHubPathId())
                .hubId(hubPath.getDepartureHub().getHubId())
                .hubName(hubPath.getDepartureHub().getName())
                .address(hubPath.getDepartureHub().getAddress())
                .nextHubId(hubPath.getArrivalHub().getHubId())
                .nextHubName(hubPath.getArrivalHub().getName())
                .nextHubAddress(hubPath.getArrivalHub().getAddress())
                .duration(hubPath.getDuration())
                .build();
    }

    public static HubPathResponseDto ofReverse(HubPath hubPath) {
        return HubPathResponseDto.builder()
                .hubPathId(hubPath.getHubPathId())
                .hubId(hubPath.getArrivalHub().getHubId())
                .hubName(hubPath.getArrivalHub().getName())
                .address(hubPath.getArrivalHub().getAddress())
                .nextHubId(hubPath.getDepartureHub().getHubId())
                .nextHubName(hubPath.getDepartureHub().getName())
                .nextHubAddress(hubPath.getDepartureHub().getAddress())
                .duration(hubPath.getDuration())
                .build();
    }

    public static HubPathResponseDto ofLastHub(Hub hub) {
        return HubPathResponseDto.builder()
                .hubId(hub.getHubId())
                .hubName(hub.getName())
                .address(hub.getAddress())
                .build();
    }
}
