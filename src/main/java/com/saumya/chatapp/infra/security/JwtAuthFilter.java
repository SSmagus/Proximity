package com.saumya.chatapp.infra.security;

import com.saumya.chatapp.auth.entity.AuthUser;
import com.saumya.chatapp.auth.repository.AuthUserRepository;
import com.saumya.chatapp.user.entity.User;
import com.saumya.chatapp.user.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final AuthUserRepository authUserRepository;
    private  final UserRepository userRepository;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.startsWith("/api/v1/auth/");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);

            try {
                Long authUserId = Long.parseLong(jwtUtil.extractSubject(token));
                AuthUser authUser =
                        authUserRepository
                                .findById(authUserId)
                                .orElseThrow();
                Optional<User> user= userRepository.findByAuthUserId(authUserId);
                // always gonna be present
                CustomUserDetails userDetails =
                        new CustomUserDetails(authUser, user.get());

                var auth =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                SecurityContextHolder
                        .getContext()
                        .setAuthentication(auth);
            } catch (Exception ignored) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }

        chain.doFilter(request, response);
    }
}
