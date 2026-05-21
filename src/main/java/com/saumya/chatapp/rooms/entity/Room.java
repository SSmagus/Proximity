package com.saumya.chatapp.rooms.entity;

import com.saumya.chatapp.rooms.enums.RoomStatus;
import com.saumya.chatapp.rooms.enums.RoomType;
import com.saumya.chatapp.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Room {
    @Id
    private String id;
    private String name;
    private String description;
    private Long creatorId;
    @Builder.Default
    private Instant createdOn=Instant.now();
    private Double latitude;
    private Double longitude;
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private RoomStatus roomStatus=RoomStatus.ACTIVE;
    @Enumerated(EnumType.STRING)
    private RoomType roomType;
    private Instant closedOn;
}
