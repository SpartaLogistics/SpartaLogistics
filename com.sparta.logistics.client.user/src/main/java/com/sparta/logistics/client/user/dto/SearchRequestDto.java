package com.sparta.logistics.client.user.dto;

import com.sparta.logistics.client.user.model.validation.MessageValid0002;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchRequestDto {
    @Schema(description = "받을 상대방의 userId", example = "2")
    @NotNull(groups = {MessageValid0002.class},
            message = "받을 상대방의 Id가 누락되었습니다."
    )
    private Long receiverId;

    @Schema(description = "검색 할 메시지", example = "message")
    @NotNull(groups = {MessageValid0002.class},
            message = "검색 할 메시지가 누락되었습니다."
    )
    private String context;
}
