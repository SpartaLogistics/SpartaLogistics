package com.sparta.logistics.client.user.model;

import com.sparta.logistics.client.user.enums.RoleType;
import com.sparta.logistics.common.model.Timestamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Entity(name = "p_users")
@Getter
@NoArgsConstructor
@ToString
public class User extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false, unique = true)
    private String slack_id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleType role;

    @Column(nullable = false)
    private Boolean isDeleted = false;

    public void softDelete() {
        this.isDeleted = true;
    }

    protected User(String username, String password, String email, String slack_id, RoleType roleType) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.slack_id = slack_id;
        this.role = roleType;
    }

    public static User createUser(String username, String password, String email, String slack_id, RoleType roleType, Boolean isDeleted) {
        return new User(username, password, email, slack_id, roleType);
    }
    public UserVO toUserVO() {
        return new UserVO(
                id, username, password,
                email, slack_id, role
                , getCreatedAt(), getUpdatedAt(), getDeletedAt(),
                getCreatedBy(), getUpdatedBy(), getDeletedBy(), isDeleted
        );
    }
}
