package com.sparta.logistics.client.user.dto;

import com.sparta.logistics.client.user.model.validation.MessageValid0001;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequestDto {
    @NotNull(groups = {MessageValid0001.class},
            message = "받을 상대방의 Id가 누락되었습니다."
    )
    private Long receiverId; //받을 상대방 ID
    @NotNull(groups = {MessageValid0001.class},
            message = "메시지가 누락되었습니다."
    )
    private String message;
}
