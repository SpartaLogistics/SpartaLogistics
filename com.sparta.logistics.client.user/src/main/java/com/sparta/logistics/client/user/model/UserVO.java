package com.sparta.logistics.client.user.model;

import com.sparta.logistics.client.user.enums.RoleType;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class UserVO implements Serializable {
    private final Long id;
    private final String username;
    private final String password;
    private final String email;
    private final String slack_id;
    private final RoleType role;

    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final LocalDateTime deletedAt;
    private final Long createdBy;
    private final Long updatedBy;
    private final Long deletedBy;

    private boolean isDeleted;

}
