package com.sparta.logistics.client.user.dto;

import com.sparta.logistics.client.user.model.validation.UserValid0002;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequestDto {
    @Schema(description = "수정 할 slackId", example = "alfn9090")
    @NotNull(groups = {UserValid0002.class},
            message = "Id가 누락되었습니다."
    )
    private String slackId;

    @Schema(description = "수정 할 이메일", example = "alfn9090@naver.com")
    @NotNull(groups = {UserValid0002.class},
            message = "Email이 누락되었습니다."
    )
    @Pattern(regexp = "^[a-zA-Z0-9+-_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$")
    private String email;
}
