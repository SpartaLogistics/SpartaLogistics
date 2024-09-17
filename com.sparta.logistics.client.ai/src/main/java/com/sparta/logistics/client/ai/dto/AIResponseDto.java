package com.sparta.logistics.client.ai.dto;

import com.sparta.logistics.client.ai.model.AI;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class AIResponseDto {

    private UUID aiId;
    private Long userId;
    private String service;
    private String question;
    private String content;
    private boolean isDeleted;

    public static AIResponseDto of(AI ai) {
        return AIResponseDto.builder()
                .userId(ai.getUserId())
                .service(ai.getService())
                .question(ai.getQuestion())
                .content(ai.getContent())
                .isDeleted(ai.isDeleted())
                .build();
    }
}
