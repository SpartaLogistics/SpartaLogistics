package com.sparta.logistics.client.hub.dto;

import com.sparta.logistics.client.hub.model.validation.HubPathValid0001;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HubPathRequestDto {

    @NotNull(groups = {HubPathValid0001.class},
            message = "출발 허브 ID 정보가 누락되었습니다."
    )
    private UUID departureHubId; // 출발 허브 ID

    @NotNull(groups = {HubPathValid0001.class},
            message = "도착 허브 ID 정보가 누락되었습니다."
    )
    private UUID arrivalHubId; // 도착 허브 ID

    @NotNull(groups = {HubPathValid0001.class},
            message = "다음 이동 허브 ID 정보가 누락되었습니다."
    )
    private UUID nextHubId; // 다음 허브 ID

    @NotNull(groups = {HubPathValid0001.class},
            message = "출발 허브 ID 정보가 누락되었습니다."
    )
    private Long duration; // 소요 시간
}
