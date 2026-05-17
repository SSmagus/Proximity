package com.saumya.chatapp.event.entity;

import com.saumya.chatapp.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

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
    private Double latitude;
    private Double longitude;
    @Builder.Default
    private Long popularity= 1L;
    @Builder.Default
    private Instant createdOn= Instant.now();
}
