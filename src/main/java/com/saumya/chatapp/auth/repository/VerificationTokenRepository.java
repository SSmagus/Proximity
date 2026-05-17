package com.saumya.chatapp.auth.repository;

import com.saumya.chatapp.auth.entity.AuthUser;
import com.saumya.chatapp.auth.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);
    void deleteByAuthUser(AuthUser authUser);
}
