package com.sparta.logistics.client.order.common.type;

public enum DeliveryStatus {
    HUB_WAITING,         // 허브 대기중
    HUB_MOVING,          // 허브 이동중
    HUB_ARRIVAL,         // 목적지 허브 도착
    IN_DELIVERY          // 배송중
}
