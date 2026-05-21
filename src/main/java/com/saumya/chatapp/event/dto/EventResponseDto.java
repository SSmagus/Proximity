package com.saumya.chatapp.event.dto;

import com.saumya.chatapp.auth.enums.EventStatus;
import com.saumya.chatapp.event.entity.Event;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class EventResponseDto {
    private Long id;
    private String name;
    private String description;
    private Double latitude;
    private Double longitude;
    private Long popularity;
    private Instant createdOn;
    private EventStatus eventStatus;
    private String roomId;
    private Instant startDate;
    private Instant closeDate;

    public static EventResponseDto from(Event event){
        return EventResponseDto.builder()
                .id(event.getId())
                .name(event.getName())
                .description(event.getDescription())
                .latitude(event.getLatitude())
                .longitude(event.getLongitude())
                .popularity(event.getPopularity())
                .createdOn(event.getCreatedOn())
                .eventStatus(event.getEventStatus())
                .roomId(event.getRoomId())
                .startDate(event.getStartDate())
                .closeDate(event.getCloseDate())
                .build();
    }
}
