package com.sparta.logistics.client.user.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigInteger;

@Entity(name = "p_user")
@Getter
@NoArgsConstructor
@ToString
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id",nullable = false)
    private BigInteger id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String slack_id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleType role;

    protected User(String username, String password, String email, String slack_id, RoleType roleType) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.slack_id = slack_id;
        this.role = roleType;
    }

    public static User createUser(String username, String password, String email, String slack_id, RoleType roleType) {
        return new User(username, password, email, slack_id, roleType);
    }
    public UserVO toUserVO() {
        return new UserVO(
                id, username, password,
                email, slack_id, role,
                getCreatedAt(), getUpdatedAt(), getDeletedAt(),
                getCreatedBy(), getUpdatedBy(), getDeletedBy(), isDeleted()
        );
    }
}
