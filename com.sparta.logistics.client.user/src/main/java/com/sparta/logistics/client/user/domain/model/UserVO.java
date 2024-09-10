package com.sparta.logistics.client.user.domain.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigInteger;
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
    private final Integer createdBy;
    private final Integer updatedBy;
    private final Integer deletedBy;

    private boolean isDeleted;

}
