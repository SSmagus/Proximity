package com.saumya.chatapp.message.dto;
import com.saumya.chatapp.message.entity.Message;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.Instant;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class MessageResponseDto {
    private String id;
    private String roomId;
    private String creatorTag;
    private Long creatorId;
    private String content;
    private Instant createdOn;

    public static MessageResponseDto from(Message message){
        return MessageResponseDto.builder()
                .id(message.getId())
                .roomId(message.getRoomId())
                .creatorTag(message.getCreatorTag())
                .creatorId(message.getCreatorId())
                .content(message.getContent())
                .createdOn(message.getCreatedOn())
                .build();
    }
}
