package com.sparta.logistics.client.hub.client;

import com.sparta.logistics.common.model.ApiResult;
import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.UUID;

@FeignClient(name = "order-service")
public interface OrderClient {

    @DeleteMapping("/orders/cancel/{productId}")
    ApiResult cancelOrder(@PathVariable("productId") UUID productId,
                          @RequestHeader("X-User-Id") String userId);
}
