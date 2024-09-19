package com.sparta.logistics.client.user.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "order-service")
public interface OrderClient {
}
