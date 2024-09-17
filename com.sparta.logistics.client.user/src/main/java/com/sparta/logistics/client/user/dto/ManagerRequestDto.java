package com.sparta.logistics.client.user.dto;

import com.sparta.logistics.client.user.enums.DeliveryManagerType;
import com.sparta.logistics.client.user.model.validation.DeliveryManagerValid0001;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ManagerRequestDto {
    @NotNull(groups = {DeliveryManagerValid0001.class},
            message = "slack_id가 누락되었습니다."
    )
    private String slackId;
    @NotNull(groups = {DeliveryManagerValid0001.class},
            message = "delveryManagerType이 누락되었습니다."
    )
    private DeliveryManagerType deliveryManagerType;
}
