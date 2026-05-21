package com.saumya.chatapp.rooms.dto;

import com.saumya.chatapp.rooms.enums.RoomType;
import com.saumya.chatapp.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomRequestDto {
    private String name;
    private String description;
    private Double latitude;
    private Double longitude;
    private RoomType roomType;
}
