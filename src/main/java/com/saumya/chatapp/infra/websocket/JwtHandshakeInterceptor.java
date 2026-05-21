package com.saumya.chatapp.infra.websocket;

import com.saumya.chatapp.auth.entity.AuthUser;
import com.saumya.chatapp.auth.repository.AuthUserRepository;
import com.saumya.chatapp.infra.security.CustomUserDetails;
import com.saumya.chatapp.infra.security.JwtUtil;
import com.saumya.chatapp.user.entity.User;
import com.saumya.chatapp.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtHandshakeInterceptor
        implements HandshakeInterceptor {

    private final JwtUtil jwtUtil;
    private final AuthUserRepository authUserRepository;
    private final UserRepository userRepository;

    @Override
    public boolean beforeHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Map<String, Object> attributes
    ) {

        String header = request
                .getHeaders()
                .getFirst("Authorization");

        if(header == null || !header.startsWith("Bearer ")) {
            return false;
        }

        String token = header.substring(7);

        try {

            Long authUserId = Long.parseLong(jwtUtil.extractSubject(token));

            AuthUser authUser = authUserRepository.findById(authUserId).orElseThrow();

            User user = userRepository.findByAuthUserId(authUserId).orElseThrow();

            CustomUserDetails userDetails = new CustomUserDetails(authUser, user);

            attributes.put("user", userDetails);

            return true;

        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void afterHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Exception exception
    ) {

    }
}