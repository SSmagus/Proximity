package com.saumya.chatapp.rooms.controller;

import com.saumya.chatapp.infra.security.CustomUserDetails;
import com.saumya.chatapp.rooms.dto.RoomRequestDto;
import com.saumya.chatapp.rooms.dto.RoomResponseDto;
import com.saumya.chatapp.rooms.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/room")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;

    @PostMapping
    public ResponseEntity<RoomResponseDto> createRoom(
            @RequestBody RoomRequestDto roomRequestDto,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
            ){
        RoomResponseDto roomResponseDto = roomService.createRoom(roomRequestDto, customUserDetails.getUser());

        return ResponseEntity.status(HttpStatus.CREATED).body(roomResponseDto);
    }

    @PatchMapping("/{roomId}")
    public ResponseEntity<RoomResponseDto> updateRoom(
            @RequestBody RoomRequestDto roomRequestDto,
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable String roomId
    ){
        RoomResponseDto roomResponseDto = roomService.updateRoom(roomId, roomRequestDto, customUserDetails.getUser());

        return ResponseEntity.status(HttpStatus.OK).body(roomResponseDto);
    }

    @GetMapping("/nearby")
    public ResponseEntity<?> getNearbyEventsByDistance(
            @RequestParam Double latitude,
            @RequestParam Double longitude
    ){
        return ResponseEntity.ok().body(roomService.getNearbyEventsByDistance(latitude, longitude));
    }

    @GetMapping("/{roomId}/messages")
    public ResponseEntity<?> getMessages(
            @PathVariable String roomId
    ){
        return ResponseEntity.ok().body(roomService.getMessagesByRoomId(roomId));
    }

    @DeleteMapping("/{roomId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void closeRoom(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable String roomId
    ){
        roomService.closeRoom(roomId, customUserDetails.getUser());
    }
}
