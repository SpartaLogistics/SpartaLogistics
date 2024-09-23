package com.sparta.logistics.common.client.dto;

import lombok.Data;

@Data
public class AIRequestDto {

    private String service;
    private String question;
    private Long userId;
}
