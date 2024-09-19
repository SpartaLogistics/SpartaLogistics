package com.sparta.logistics.client.user.dto;

import com.sparta.logistics.client.user.model.validation.MessageValid0001;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "받을 상대방의 Id", example = "1")
    @NotNull(groups = {MessageValid0001.class},
            message = "받을 상대방의 Id가 누락되었습니다."
    )
    private Long receiverId; //받을 상대방 ID

    @Schema(description = "메시지", example = "message")
    @NotNull(groups = {MessageValid0001.class},
            message = "메시지가 누락되었습니다."
    )
    private String message;
}
