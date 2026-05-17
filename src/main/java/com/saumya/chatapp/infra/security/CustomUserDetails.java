package com.saumya.chatapp.infra.security;

import com.saumya.chatapp.auth.entity.AuthUser;
import com.saumya.chatapp.user.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;

// works for final & not null fields
@RequiredArgsConstructor
@Getter
public class CustomUserDetails
        implements UserDetails {

    private final AuthUser authUser;
    private final User user;

    public Long getAuthId() {
        return authUser.getId();
    }

    public Long getId() {
        return user.getId();
    }

    public String getEmail() {
        return authUser.getEmail();
    }

    @Override
    public Collection<? extends GrantedAuthority>
    getAuthorities() {
        return List.of(
                new SimpleGrantedAuthority("ROLE_USER")
        );
    }

    @Override
    public String getPassword() {
        return authUser.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return authUser.getEmail();
    }
}
