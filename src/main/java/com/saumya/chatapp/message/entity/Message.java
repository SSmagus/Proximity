package com.saumya.chatapp.message.entity;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document
@CompoundIndexes({
        @CompoundIndex(name = "room_created_idx", def = "{'roomId': 1, 'createdAt': -1}")
})
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    @Id
    private String id;
    private String roomId;
    private Long creatorId;
    private String creatorTag;
    private String content;
    @Builder.Default
    private Instant createdOn=Instant.now();
}
