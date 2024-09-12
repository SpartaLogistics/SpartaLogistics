package com.sparta.logistics.client.hub.dto;

import com.sparta.logistics.client.hub.model.validation.HubValid0001;
import com.sparta.logistics.client.hub.model.validation.HubValid0002;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(groups = {HubValid0001.class, HubValid0002.class},
            message = "허브명 정보가 누락되었습니다."
    )
    private String name;          // 허브 이름

    @NotNull(groups = {HubValid0001.class, HubValid0002.class},
            message = "허브 주소 정보가 누락되었습니다."
    )
    private String address;       // 허브 주소

    @NotNull(groups = {HubValid0001.class},
            message = "위도 정보가 누락되었습니다."
    )
    private BigDecimal latitude;  // 위도

    @NotNull(groups = {HubValid0001.class},
            message = "경도 정보가 누락되었습니다."
    )
    private BigDecimal longitude; // 경도
}
