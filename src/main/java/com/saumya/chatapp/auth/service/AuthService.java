package com.saumya.chatapp.auth.service;

import com.saumya.chatapp.infra.security.JwtUtil;
import com.saumya.chatapp.auth.dto.LoginRequest;
import com.saumya.chatapp.auth.dto.RegisterRequest;
import com.saumya.chatapp.auth.dto.ResetPasswordRequest;
import com.saumya.chatapp.auth.entity.AuthUser;
import com.saumya.chatapp.auth.entity.VerificationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthUserService authUserService;
    private final TokenService tokenService;
    private final EmailService emailService;
    private final JwtUtil jwtUtil;

    @Transactional
    public void register(RegisterRequest request, String verifyLinkBase) {
        AuthUser authUser = authUserService.register(request.getEmail(), request.getPassword());
        VerificationToken token = tokenService.createToken(authUser);
        String link = verifyLinkBase + token.getToken();
        String html = "<p>Click the link to verify your account:</p><a href=\"" + link + "\">Verify</a>";
        emailService.sendHtml(authUser.getEmail(), "Verify your account", html);
    }

    public String login(LoginRequest request) {
        AuthUser authUser = authUserService.getByEmail(request.getEmail()).orElse(null);
        if (authUser == null) return null;
        if (!authUser.isVerified()) return null;
        if (!authUserService.checkPassword(authUser, request.getPassword())) return null;
        return jwtUtil.generateToken(authUser.getId().toString());
    }

    public boolean verifyAccount(String tokenValue) {
        VerificationToken token = tokenService.verifyToken(tokenValue);
        if (token == null) return false;
        authUserService.markVerified(token.getAuthUser());
        tokenService.markUsed(token);
        return true;
    }

    public void requestPasswordReset(String email, String resetLinkBase) {
        AuthUser authUser = authUserService.getByEmail(email).orElse(null);
        if (authUser == null) return;
        VerificationToken token = tokenService.createToken(authUser);
        String link = resetLinkBase + token.getToken();
        String html = "<p>Reset your password:</p><a href=\"" + link + "\">Reset Password</a>";
        emailService.sendHtml(email, "Reset Password", html);
    }

    public boolean resetPassword(ResetPasswordRequest request) {
        VerificationToken token = tokenService.verifyToken(request.getToken());
        if (token == null) return false;
        authUserService.updatePassword(token.getAuthUser(), request.getNewPassword());
        tokenService.markUsed(token);
        return true;
    }
}
