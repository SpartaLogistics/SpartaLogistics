package com.sparta.logistics.client.user.dto;

import com.sparta.logistics.client.user.model.Message;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse {
    private String receiver_slack_id;
    private String sender_slack_id;
    private String message;

    public static MessageResponse from(Message message) {
        return MessageResponse.builder()
                .receiver_slack_id(message.getReceiver().getSlack_id())
                .sender_slack_id(message.getSender().getSlack_id())
                .message(message.getMessage())
                .build();
    }
}
