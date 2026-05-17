package com.saumya.chatapp.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class EventRequestDto {
    private String name;
    private String description;
    private Double latitude;
    private Double longitude;
}
