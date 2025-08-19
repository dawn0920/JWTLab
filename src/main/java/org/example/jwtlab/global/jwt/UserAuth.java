package org.example.jwtlab.global.jwt;

import lombok.Getter;
import org.example.jwtlab.domain.user.enums.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
public class UserAuth implements UserDetails {

    private final Long userId;
    private final String email;
    private final String password;
    private final String name;
    private final UserRole userRole;

    public UserAuth(Long userId, String email, String password, String name, UserRole userRole) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.name = name;
        this.userRole = userRole;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + userRole.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override public boolean isAccountNonExpired() {
        return true;
    }
    @Override public boolean isAccountNonLocked() {
        return true;
    }
    @Override public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override public boolean isEnabled() {
        return true;
    }
}
