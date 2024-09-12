package com.sparta.logistics.client.user.dto;

import com.sparta.logistics.client.user.enums.DeliveryManagerType;
import com.sparta.logistics.client.user.model.DeliveryManager;
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
    private UUID delivery_manager_id;
    private String slack_id;
    private DeliveryManagerType delivery_manager_type;
    private Boolean is_deleted;

    public static DeliveryManagerResponse of(DeliveryManager deliveryManager) {
        return DeliveryManagerResponse.builder()
                .delivery_manager_id(deliveryManager.getId())
                .slack_id(deliveryManager.getSlack_id())
                .delivery_manager_type(deliveryManager.getType())
                .is_deleted(deliveryManager.isDeleted())
                .build();
    }
}
