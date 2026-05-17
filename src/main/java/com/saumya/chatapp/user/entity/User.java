package com.saumya.chatapp.user.entity;

import com.saumya.chatapp.auth.entity.AuthUser;
import com.saumya.chatapp.event.entity.Event;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.List;

@Entity(name = "users")
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "user_id")
    private Long id;
    @Column(unique = true, nullable = false)
    private String tag;
    private String name;
    private String description;
    private Instant createdAt;
    @OneToOne
    @JoinColumn(name = "auth_user_id")
    private AuthUser authUser;
    @Builder.Default
    private Boolean canChangeTag=true;
    private Long latitude;
    private Long longitude;
    @OneToMany(mappedBy = "creator")
    private List<Event> events;
}
