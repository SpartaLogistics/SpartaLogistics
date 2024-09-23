package com.sparta.logistics.client.user.client;

import com.sparta.logistics.common.model.ApiResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "hub-service")
public interface HubClient {

    @GetMapping("/hubs/delivery_managers/{hubName}")
    ApiResult getDeliveryManagers(@PathVariable("hubName") String name);
    
}
