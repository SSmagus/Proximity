package com.saumya.chatapp.message.service;
import com.saumya.chatapp.message.dto.MessageResponseDto;
import com.saumya.chatapp.message.entity.Message;
import com.saumya.chatapp.message.repository.MessageRepository;
import com.saumya.chatapp.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public MessageResponseDto save(String roomId, Long userId, String content){
        String tag= userRepository.findById(userId).orElseThrow().getName();
        Message message= Message.builder()
                .content(content)
                .roomId(roomId)
                .creatorId(userId)
                .creatorTag(tag)
                .build();
        Message saved= messageRepository.save(message);
        return MessageResponseDto.from(saved);
    }
}
