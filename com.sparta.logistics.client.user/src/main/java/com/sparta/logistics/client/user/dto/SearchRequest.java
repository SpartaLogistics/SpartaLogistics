package com.sparta.logistics.client.user.dto;

import com.sparta.logistics.client.user.model.validation.MessageValid0001;
import com.sparta.logistics.client.user.model.validation.MessageValid0002;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchRequest {
    @NotNull(groups = {MessageValid0002.class},
            message = "받을 상대방의 Id가 누락되었습니다."
    )
    private Long receiverId;
    @NotNull(groups = {MessageValid0002.class},
            message = "받을 상대방의 Id가 누락되었습니다."
    )
    private String context;
}
