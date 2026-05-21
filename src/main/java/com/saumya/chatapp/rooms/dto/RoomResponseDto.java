package com.saumya.chatapp.rooms.dto;

import com.saumya.chatapp.event.dto.EventResponseDto;
import com.saumya.chatapp.rooms.entity.Room;
import com.saumya.chatapp.rooms.enums.RoomStatus;
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
@Builder
public class RoomResponseDto {
    private String id;
    private String name;
    private String description;
    private Long creatorId;
    private Instant createdOn;
    private Double latitude;
    private Double longitude;
    private RoomStatus roomStatus;
    private RoomType roomType;
    private Instant closedOn;

    public static RoomResponseDto from(Room room){
        return RoomResponseDto.builder()
                .id(room.getId())
                .name(room.getName())
                .description(room.getDescription())
                .latitude(room.getLatitude())
                .longitude(room.getLongitude())
                .creatorId(room.getCreatorId())
                .createdOn(room.getCreatedOn())
                .roomStatus(room.getRoomStatus())
                .roomType(room.getRoomType())
                .closedOn(room.getClosedOn())
                .build();
    }
}
