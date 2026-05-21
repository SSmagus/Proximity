package com.saumya.chatapp.message.controller;

import com.saumya.chatapp.infra.security.CustomUserDetails;
import com.saumya.chatapp.message.dto.MessageRequestDto;
import com.saumya.chatapp.message.dto.MessageResponseDto;
import com.saumya.chatapp.message.entity.Message;
import com.saumya.chatapp.message.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/sendMessage/{roomId}")
    public void sendMessage(
            @DestinationVariable String roomId,
            MessageRequestDto request,
            SimpMessageHeaderAccessor accessor
    ) {

        CustomUserDetails userDetails = (CustomUserDetails) accessor.getSessionAttributes().get("user");

        MessageResponseDto saved = messageService.save(
                roomId,
                userDetails.getId(),
                request.getContent()
        );

        messagingTemplate.convertAndSend(
                "/topic/room/" + roomId,
                saved
        );
    }
}
