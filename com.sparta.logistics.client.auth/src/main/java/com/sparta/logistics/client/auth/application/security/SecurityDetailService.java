package com.sparta.logistics.client.auth.application.security;

import com.sparta.logistics.client.auth.domain.model.UserVO;
import com.sparta.logistics.client.auth.infrastructure.feign.UserClient;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class SecurityDetailService implements UserDetailsService {

    UserClient userClient;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {

        SecurityUserInfo securityUserInfo = getSecurityUserInfo(id);
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(securityUserInfo.getRole().name()));
        return new SecurityUserDetails(securityUserInfo, authorities);
    }

    private SecurityUserInfo getSecurityUserInfo(String username) throws UsernameNotFoundException {

        UserVO user = userClient.findByUsername(username);

        if(user.isDeleted()) {
            throw new UsernameNotFoundException("This account is unavailable");
        }

        SecurityUserInfo securityUserInfo = toSecurityUserInfo(user);

        return securityUserInfo;

    }

    public SecurityUserInfo toSecurityUserInfo(UserVO user) {
        return new SecurityUserInfo(
                user.getId(), user.getUsername(), user.getPassword(),
                user.getEmail(), user.getSlack_id(),
                user.getRole()
        );
    }

}
