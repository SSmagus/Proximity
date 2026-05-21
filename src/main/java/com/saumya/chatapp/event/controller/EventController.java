package com.saumya.chatapp.event.controller;

import com.saumya.chatapp.infra.security.CustomUserDetails;
import com.saumya.chatapp.event.dto.EventRequestDto;
import com.saumya.chatapp.event.dto.EventResponseDto;
import com.saumya.chatapp.event.service.EventService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/v1/event")
@AllArgsConstructor
public class EventController {
    private final EventService eventService;

    @PostMapping
    public ResponseEntity<EventResponseDto> createEvent(@RequestBody EventRequestDto eventRequestDto,
                                                        @AuthenticationPrincipal CustomUserDetails customUserDetails){
        EventResponseDto eventResponseDto= eventService.createEvent(eventRequestDto, customUserDetails.getUser());
        return ResponseEntity.status(HttpStatus.CREATED).body(eventResponseDto);
    }

    // list of events created by user
    @GetMapping
    public ResponseEntity<?> getAllEventsByUser(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        List<EventResponseDto> eventResponseDtos = eventService.getAllEventsByUser(customUserDetails.getUser());
        return ResponseEntity.ok(eventResponseDtos);
    }

    // increase decrease popularity, need to explicitly handle race and unique condition
    //PATCH /events/1/vote?type=UPVOTE
    // body contains  type: "upvote/downvote"
    @PatchMapping("/{eventId}/vote")
    public ResponseEntity<EventResponseDto> vote(@RequestParam String type,
                                                 @PathVariable Long eventId,
                                                 @AuthenticationPrincipal CustomUserDetails customUserDetails){
        EventResponseDto eventResponseDto= eventService.updateEventPopularity(type, eventId, customUserDetails.getUser());
        return ResponseEntity.status(HttpStatus.OK).body(eventResponseDto);
    }

    /*
    * not using user location, since location update might race against nearby event
    * to update location otherwise would lead to coupling
    * */
    @GetMapping("/nearby")
    public ResponseEntity<?> getNearbyEvents(
            @RequestParam double latitude,
            @RequestParam double longitude
    ){
        return ResponseEntity.ok(eventService.findNearbyEvents(latitude, longitude));
    }

    @DeleteMapping("/{eventId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void closeEventById(
            @PathVariable Long eventId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        eventService.closeEventById(customUserDetails.getUser() ,eventId);

    }
}
