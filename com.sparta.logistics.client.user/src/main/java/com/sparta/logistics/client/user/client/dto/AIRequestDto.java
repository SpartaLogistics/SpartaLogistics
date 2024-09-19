package com.sparta.logistics.client.user.client.dto;

import lombok.Data;

@Data
public class AIRequestDto {
    private Long userId;
    private String service;
    private String question;
}
