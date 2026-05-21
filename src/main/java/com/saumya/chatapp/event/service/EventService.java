package com.saumya.chatapp.event.service;

import com.saumya.chatapp.auth.enums.EventStatus;
import com.saumya.chatapp.event.dto.EventRequestDto;
import com.saumya.chatapp.event.dto.EventResponseDto;
import com.saumya.chatapp.event.entity.Event;
import com.saumya.chatapp.event.repository.EventRepository;
import com.saumya.chatapp.infra.redis.RedisGeoEventService;
import com.saumya.chatapp.rooms.entity.Room;
import com.saumya.chatapp.rooms.service.RoomService;
import com.saumya.chatapp.user.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final RedisGeoEventService redisGeoEventService;
    private final RoomService roomService;

    @Transactional
    public EventResponseDto createEvent(EventRequestDto eventRequestDto, User user) {
        Event event= Event.builder()
                .name(eventRequestDto.getName())
                .description(eventRequestDto.getDescription())
                .creator(user)
                .latitude(eventRequestDto.getLatitude())
                .longitude(eventRequestDto.getLongitude())
                .startDate(eventRequestDto.getStartDate())
                .closeDate(eventRequestDto.getCloseDate())
                .build();
        event = eventRepository.save(event);
        Room room= roomService.createEventRoom(event);
        event.setRoomId(room.getId());
        event = eventRepository.save(event);
        redisGeoEventService.addEvent(event.getId(),
                event.getLongitude(),
                event.getLatitude());
        return EventResponseDto.from(event);
    }

    public EventResponseDto updateEventPopularity(String vote, Long eventId, User user){
        Optional<Event> event= eventRepository.findById(eventId);
        if(event.isEmpty()){
            throw new RuntimeException("event not created by user");
        }
        Long prevPopularity=event.get().getPopularity();
        if(vote.equalsIgnoreCase("upvote")){
            event.get().setPopularity(prevPopularity+1);
        }
        else event.get().setPopularity(prevPopularity-1);

        eventRepository.save(event.get());

        return EventResponseDto.from(event.get());
    }

    public List<EventResponseDto> getAllEventsByUser(User user){
        List<Event> events=eventRepository.findAllEventsByCreator(user);
        return events.stream().map(
                EventResponseDto::from
        ).toList();
    }

    // need to add paging mechanism or maybe on user side
    // also need to provide distance based ui
    public List<EventResponseDto> findNearbyEvents(double latitude, double longitude){
        List<Long> eventIds=redisGeoEventService.getNearbyEventIds(
                longitude,latitude);
        return eventIds.stream().map(
                eventId->EventResponseDto.from(eventRepository.getReferenceById(eventId))
        ).toList();
    }

    @Transactional
    public void closeEventById(User user, Long eventId){
        Optional<Event> event= eventRepository.findById(eventId);
        if(event.isEmpty()) throw new RuntimeException("Event doesn't exist, can't delete");
        if(event.get().getEventStatus().equals(EventStatus.CLOSED)) throw new RuntimeException("Event already closed");
        if(!event.get().getCreator().getId().equals(user.getId())) throw new RuntimeException("User is not authorized to delete this event");

        event.get().setEventStatus(EventStatus.CLOSED);
        eventRepository.save(event.get());
        roomService.closeRoom(event.get().getRoomId(), user);
        redisGeoEventService.removeEvent(eventId);
    }
}
