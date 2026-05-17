package com.saumya.chatapp.user.controller;

import com.saumya.chatapp.infra.security.CustomUserDetails;
import com.saumya.chatapp.user.dto.UserRequestDto;
import com.saumya.chatapp.user.dto.UserResponseDto;
import com.saumya.chatapp.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PatchMapping("/me")
    public ResponseEntity<?> changeName(@RequestBody UserRequestDto userRequestDto,
                                     @AuthenticationPrincipal CustomUserDetails customUserDetails){
        UserResponseDto userResponseDto= userService.updateDetails(userRequestDto, customUserDetails.getId());
        return ResponseEntity.ok(userResponseDto);
    }

    @PatchMapping("/me/tag")
    public ResponseEntity<?> changeTag(@RequestBody UserRequestDto userRequestDto,
                                     @AuthenticationPrincipal CustomUserDetails customUserDetails){
        UserResponseDto userResponseDto= userService.updateTag(userRequestDto, customUserDetails.getId());
        return ResponseEntity.ok(userResponseDto);
    }
}
