package com.saumya.chatapp.event.entity;

import com.saumya.chatapp.auth.enums.EventStatus;
import com.saumya.chatapp.rooms.entity.Room;
import com.saumya.chatapp.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Reference;

import java.time.Instant;

@Entity
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String description;
    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User creator;
    @JoinColumn(name = "event_room_id")
    private String roomId;
    private Double latitude;
    private Double longitude;
    @Builder.Default
    private Long popularity= 1L;
    @Builder.Default
    private Instant createdOn= Instant.now();
    @Builder.Default
    private EventStatus eventStatus=EventStatus.ACTIVE;
    private Instant startDate;
    private Instant closeDate;
}
