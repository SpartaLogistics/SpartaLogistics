package com.sparta.logistics.client.ai.dto;

import com.sparta.logistics.client.ai.model.validation.AIValid0001;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AIRequestDto {

    @NotNull(groups = {AIValid0001.class},
            message = "호출 서비스가 누락되었습니다.")
    private String service;

    @NotNull(groups = {AIValid0001.class},
            message = "질문이 누락되었습니다.")
    private String question;

}
