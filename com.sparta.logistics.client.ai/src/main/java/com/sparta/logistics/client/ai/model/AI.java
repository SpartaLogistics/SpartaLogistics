package com.sparta.logistics.client.ai.model;


import com.sparta.logistics.client.ai.dto.AIRequestDto;
import com.sparta.logistics.common.model.Timestamped;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Builder
@Table(name = "p_ai")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AI extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ai_id", columnDefinition = "UUID")
    private UUID aiId;
    private Long userId;
    private String service;     // 호출 서비스

    @Lob
    private String question;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    private boolean isDeleted;

    @Builder(builderMethodName = "AICreateBuilder", builderClassName = "AICreateBuilder")
    public AI(Long userId, AIRequestDto requestDto) {
        this.userId = userId;
        this.service = requestDto.getService();
        this.question = requestDto.getQuestion();
        this.content = requestDto.getContent();
        this.isDeleted = false;
    }

    public void softDelete() {
        this.isDeleted = true;
    }
}
