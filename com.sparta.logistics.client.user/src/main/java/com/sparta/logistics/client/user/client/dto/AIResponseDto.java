package com.sparta.logistics.client.user.client.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class AIResponseDto {

    private UUID aiId;
    private Long userId;
    private String service;
    private String question;
    private String content;
}
