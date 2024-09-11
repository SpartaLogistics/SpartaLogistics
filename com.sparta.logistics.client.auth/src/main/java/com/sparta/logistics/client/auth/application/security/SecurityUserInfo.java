package com.sparta.logistics.client.auth.application.security;

import com.sparta.logistics.client.auth.domain.model.RoleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SecurityUserInfo {

    private Long id;
    private String username;
    private String password;

    private String email;
    private String slack_id;

    private RoleType role;

}