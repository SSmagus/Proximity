package com.saumya.chatapp.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.Instant;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto {
    private String tag;
    private String name;
    private String description;
    private Boolean canChangeTag;
    private Instant createdAt;
}
