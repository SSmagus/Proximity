package com.saumya.chatapp.auth.service;

import com.saumya.chatapp.auth.entity.AuthUser;
import com.saumya.chatapp.auth.entity.VerificationToken;
import com.saumya.chatapp.auth.repository.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final VerificationTokenRepository tokenRepository;

    @Value("${app.token.expiry-minutes}")
    private long expiryMinutes;

    public VerificationToken createToken(AuthUser authUser) {
        tokenRepository.deleteByAuthUser(authUser);
        VerificationToken token = new VerificationToken();
        token.setAuthUser(authUser);
        token.setToken(UUID.randomUUID().toString());
        token.setExpiresAt(Instant.now().plusSeconds(expiryMinutes * 60));
        token.setUsed(false);
        return tokenRepository.save(token);
    }

    public VerificationToken verifyToken(String tokenValue) {
        return tokenRepository.findByToken(tokenValue)
                .filter(t -> !t.isUsed())
                .filter(t -> t.getExpiresAt().isAfter(Instant.now()))
                .orElse(null);
    }

    public void markUsed(VerificationToken token) {
        token.setUsed(true);
        tokenRepository.save(token);
    }
}
