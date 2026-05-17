package com.saumya.chatapp.auth.service;

import com.saumya.chatapp.auth.entity.AuthUser;
import com.saumya.chatapp.auth.repository.AuthUserRepository;
import com.saumya.chatapp.user.entity.User;
import com.saumya.chatapp.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthUserService {

    private final AuthUserRepository authUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    public AuthUser register(String email, String password) {

        Optional<AuthUser> existing = authUserRepository.findByEmail(email);

        if(existing.isPresent()) {
            AuthUser authUser = existing.get();
            if(authUser.isVerified()) {
                throw new RuntimeException("User already verified");
            }
            authUser.setPasswordHash(passwordEncoder.encode(password));
            return authUserRepository.save(authUser);
        }

        AuthUser authUser = new AuthUser();

        authUser.setEmail(email.toLowerCase());
        authUser.setPasswordHash(passwordEncoder.encode(password));
        authUser.setVerified(false);

        return authUserRepository.save(authUser);
    }

    public Optional<AuthUser> getByEmail(String email) {
        return authUserRepository.findByEmail(email.toLowerCase());
    }

    public boolean checkPassword(AuthUser authUser, String password) {
        return passwordEncoder.matches(password, authUser.getPasswordHash());
    }


    public void markVerified(AuthUser authUser) {
        authUser.setVerified(true);
        authUserRepository.save(authUser);
        userService.buildDefaultUser(authUser);
    }

    public void updatePassword(AuthUser authUser, String newPassword) {
        authUser.setPasswordHash(passwordEncoder.encode(newPassword));
        authUserRepository.save(authUser);
    }
}
