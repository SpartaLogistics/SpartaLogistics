package com.sparta.logistics.client.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequestDto {
    private String slackId; //받을 상대방 ID
    private String message;
}
