package com.sparta.logistics.client.hub.client;

import com.sparta.logistics.client.hub.client.dto.AiRequestDto;
import com.sparta.logistics.common.model.ApiResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ai-service")
public interface AiClient {

    @PostMapping("/ai")
    ApiResult createAI(@RequestBody AiRequestDto aiRequestDto);


}
