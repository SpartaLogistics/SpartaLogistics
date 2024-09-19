package com.sparta.logistics.client.user.dto;

import com.sparta.logistics.client.user.enums.RoleType;
import com.sparta.logistics.client.user.model.validation.UserValid0001;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {
    @Size(min = 4, max = 10)
    @Pattern(regexp = "^[a-z0-9]+$")
    @NotNull(groups = {UserValid0001.class},
            message = "Id가 누락되었습니다."
    )
    @Schema(description = "회원 아이디", example = "alfn8989")
    private String username;

    @Schema(description = "회원 비밀번호", example = "!asdf1128@")
    @Size(min = 8, max = 15)
    @NotNull(groups = {UserValid0001.class},
            message = "Password가 누락되었습니다."
    )
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[\\W_]).+$")
    private String password;

    @Schema(description = "회원 이메일", example = "1rhgurwls1@naver.com")
    @NotNull(groups = {UserValid0001.class},
            message = "Email이 누락되었습니다."
    )
    @Pattern(regexp = "^[a-zA-Z0-9+-_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$")
    private String email;

    @Schema(description = "슬랙 아이디", example = "slackid")
    @NotNull(groups = {UserValid0001.class},
            message = "slackId가 누락되었습니다."
    )
    private String slackId;

    @Schema(description = "회원 직책", example = "CUSTOMER")
    @NotNull(groups = {UserValid0001.class},
            message = "Role이 누락되었습니다."
    )
    private RoleType role;
}