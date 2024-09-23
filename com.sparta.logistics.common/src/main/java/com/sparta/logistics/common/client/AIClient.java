package com.sparta.logistics.common.client;

import com.sparta.logistics.common.client.dto.AIRequestDto;
import com.sparta.logistics.common.model.ApiResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ai-service")
public interface AIClient {

    @PostMapping("/ai")
    ApiResult createAI(@RequestBody @Validated AIRequestDto aiRequestDto);
}
