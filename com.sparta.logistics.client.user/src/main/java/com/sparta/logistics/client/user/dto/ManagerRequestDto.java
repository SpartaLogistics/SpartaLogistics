package com.sparta.logistics.client.user.dto;

import com.sparta.logistics.client.user.enums.DeliveryManagerType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ManagerRequestDto {
    private String slackId;
    private DeliveryManagerType deliveryManagerType;
}
