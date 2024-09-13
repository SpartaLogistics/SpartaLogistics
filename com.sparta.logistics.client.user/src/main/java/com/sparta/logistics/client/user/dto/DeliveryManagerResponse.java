package com.sparta.logistics.client.user.dto;

import com.sparta.logistics.client.user.enums.DeliveryManagerType;
import com.sparta.logistics.client.user.model.DeliveryManager;
import com.sparta.logistics.client.user.model.validation.DeliveryManagerValid0001;
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
public class DeliveryManagerResponse {
    @NotNull(groups = {DeliveryManagerValid0001.class},
            message = "delivery_manager_id가 누락되었습니다."
    )
    private UUID delivery_manager_id;
    @NotNull(groups = {DeliveryManagerValid0001.class},
            message = "slack_id가 누락되었습니다."
    )
    private String slack_id;
    @NotNull(groups = {DeliveryManagerValid0001.class},
            message = "manager_type이 누락되었습니다."
    )
    private DeliveryManagerType delivery_manager_type;

    public static DeliveryManagerResponse of(DeliveryManager deliveryManager) {
        return DeliveryManagerResponse.builder()
                .delivery_manager_id(deliveryManager.getId())
                .slack_id(deliveryManager.getSlack_id())
                .delivery_manager_type(deliveryManager.getType())
                .build();
    }
}
