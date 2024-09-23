package com.sparta.logistics.client.ai.dto;

import lombok.Data;

@Data
public class AISearchCriteria {

    private String service;
    private String question;
    private Long userId;
}
