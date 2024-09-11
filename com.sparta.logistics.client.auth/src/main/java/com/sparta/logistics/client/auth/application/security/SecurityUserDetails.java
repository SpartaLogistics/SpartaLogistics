package com.sparta.logistics.client.auth.application.security;

import com.sparta.logistics.client.auth.domain.model.RoleType;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@Slf4j
public class SecurityUserDetails implements UserDetails {
    private final SecurityUserInfo securityUserInfo;
    private final List<? extends GrantedAuthority> authorities;

    public SecurityUserDetails(SecurityUserInfo securityUserInfo, List<? extends GrantedAuthority> authorities) {
        this.securityUserInfo = securityUserInfo;
        this.authorities = authorities;
    }

    @Override
    public String getUsername() {
        return securityUserInfo.getUsername();
    }

    @Override
    public String getPassword() {
        return securityUserInfo.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public Long getId() {
        return securityUserInfo.getId();
    }

    public String getEmail() {
        return securityUserInfo.getEmail();
    }

    public String getSlackId() {
        return securityUserInfo.getSlack_id();
    }

    public RoleType getRoleType() {
        return securityUserInfo.getRole();
    }


    /**
     * 필요시 재정의(기본 RETURN TRUE)
     */
    @Override
    public boolean isAccountNonExpired() {
        return Boolean.TRUE;
    }

    @Override
    public boolean isAccountNonLocked() {
        return Boolean.TRUE;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return Boolean.TRUE;
    }

    @Override
    public boolean isEnabled() {
        return Boolean.TRUE;
    }
}
