package com.sparta.logistics.client.user.dto;

import com.sparta.logistics.client.user.enums.RoleType;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    @Size(min = 4, max = 10)
    @Pattern(regexp = "^[a-z0-9]+$")
    private String username;
    @Size(min = 8, max = 15)
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[\\W_]).+$")
    private String password;
    @Pattern(regexp = "^[a-zA-Z0-9+-_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$")
    private String email;
    private String slackId;
    private RoleType role;
}