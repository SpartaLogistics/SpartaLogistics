package com.sparta.logistics.client.auth.presentation.request;

import com.sparta.logistics.client.auth.domain.model.RoleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    private String username;
    private String password;
    private String email;
    private String slackId;
    private RoleType role;
}