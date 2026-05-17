package com.saumya.chatapp.auth.service;

import com.saumya.chatapp.auth.entity.AuthUser;
import com.saumya.chatapp.auth.repository.AuthUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AuthUserRepository authUserRepository;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        AuthUser authUser = authUserRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));

        return org.springframework.security.core.userdetails.User
                .withUsername(authUser.getEmail())
                .password(authUser.getPasswordHash())
                .roles("USER")
                .build();
    }
}