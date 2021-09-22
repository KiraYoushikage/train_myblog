package com.kirayous.common.auth;

import com.kirayous.common.dto.UserInfoDTO;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author kirayous
 * @version v1.0
 * @package com.kirayous.auth
 * @date 2021/8/23 15:04
 */
@Data
@Builder
public class MyUserDetails implements UserDetails {



    private UserInfoDTO user;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
//        System.out.println(this.getUser().getRoleList().toString());
        if (Objects.isNull(this.user.getRoleList()))return null;
        return this.user.getRoleList().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return this.getUser().getPassword();
    }

    @Override
    public String getUsername() {
        return this.getUser().getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
