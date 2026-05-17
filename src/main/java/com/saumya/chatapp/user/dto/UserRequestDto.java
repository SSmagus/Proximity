package com.saumya.chatapp.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class UserRequestDto {
    private String tag;
    private String name;
    private String description;
}
