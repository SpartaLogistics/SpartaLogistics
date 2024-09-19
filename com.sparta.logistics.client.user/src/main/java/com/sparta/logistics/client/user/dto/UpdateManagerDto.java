package com.sparta.logistics.client.user.dto;

import com.sparta.logistics.client.user.enums.DeliveryManagerType;
import com.sparta.logistics.client.user.model.validation.DeliveryManagerValid0002;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateManagerDto {
    @Schema(description = "slack_id", example = "alfn8989")
    @NotNull(groups = {DeliveryManagerValid0002.class},
            message = "slack_id가 누락되었습니다."
    )
    private String slackId;

    @Schema(description = "매니저 타입", example = "HUB_DELIVERY")
    @NotNull(groups = {DeliveryManagerValid0002.class},
            message = "deliveryManagerType이 누락되었습니다."
    )
    private DeliveryManagerType deliveryManagerType;
}
