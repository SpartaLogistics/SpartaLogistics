package com.sparta.logistics.client.user.dto;

import com.sparta.logistics.client.user.model.validation.UserValid0002;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequestDto {
    @NotNull(groups = {UserValid0002.class},
            message = "Id가 누락되었습니다."
    )
    private String slackId;

    @NotNull(groups = {UserValid0002.class},
            message = "Email이 누락되었습니다."
    )
    @Pattern(regexp = "^[a-zA-Z0-9+-_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$")
    private String email;
}
