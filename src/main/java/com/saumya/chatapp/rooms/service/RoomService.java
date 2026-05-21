package com.saumya.chatapp.rooms.service;

import com.saumya.chatapp.event.entity.Event;
import com.saumya.chatapp.infra.redis.RedisGeoRoomService;
import com.saumya.chatapp.message.dto.MessageResponseDto;
import com.saumya.chatapp.message.entity.Message;
import com.saumya.chatapp.message.repository.MessageRepository;
import com.saumya.chatapp.message.service.MessageService;
import com.saumya.chatapp.rooms.dto.RoomRequestDto;
import com.saumya.chatapp.rooms.dto.RoomResponseDto;
import com.saumya.chatapp.rooms.entity.Room;
import com.saumya.chatapp.rooms.enums.RoomStatus;
import com.saumya.chatapp.rooms.enums.RoomType;
import com.saumya.chatapp.rooms.repository.RoomRepository;
import com.saumya.chatapp.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final RedisGeoRoomService redisGeoRoomService;
    private final MessageRepository messageRepository;

    private <T> T pick(T newVal, T oldVal){
        return newVal != null ? newVal : oldVal;
    }

    public Room createEventRoom(Event event){
        Room room= Room.builder()
                .name(event.getName())
                .description("Official event room")
                .creatorId(event.getCreator().getId())
                .latitude(event.getLatitude())
                .longitude(event.getLongitude())
                .roomType(RoomType.EVENT)
                .build();

        room = roomRepository.save(room);

        return room;
    }

    public RoomResponseDto createRoom(RoomRequestDto roomRequestDto, User user){
        Room room= Room.builder()
                .name(roomRequestDto.getName())
                .description(roomRequestDto.getDescription())
                .latitude(roomRequestDto.getLatitude())
                .longitude(roomRequestDto.getLongitude())
                .creatorId(user.getId())
                .roomType(roomRequestDto.getRoomType())
                .build();

        room = roomRepository.save(room);
        redisGeoRoomService.addRoom(
                room.getId(),
                room.getLongitude(),
                room.getLatitude());
        //redis work
        return RoomResponseDto.from(room);
    }
    /*
    * Dosnt care about room status, keeping it simple at the moment... need future changes
    * */
    public RoomResponseDto updateRoom(String roomId, RoomRequestDto roomRequestDto, User user){
        Optional<Room> opRoom = roomRepository.findById(roomId);
        if(opRoom.isEmpty()) throw new RuntimeException("Room dosnt exist");
        Room room= opRoom.get();
        if(!Objects.equals(room.getCreatorId(), user.getId())) throw new RuntimeException("User is not the owner of room");
        if(room.getRoomStatus().equals(RoomStatus.CLOSED)) throw new RuntimeException("Room already closed");
        room.setName(pick(roomRequestDto.getName(), room.getName()));
        room.setDescription(pick(roomRequestDto.getDescription(), room.getDescription()));
        // no point in changing these
//        room.setLatitude(pick(roomRequestDto.getLatitude(), room.getLatitude()));
//        room.setLongitude(pick(roomRequestDto.getLongitude(), room.getLongitude()));

        roomRepository.save(room);

        return RoomResponseDto.from(room);
    }

    public List<RoomResponseDto> getNearbyEventsByDistance(Double latitude, Double longitude){
        List<String> roomIds= redisGeoRoomService.getNearbyRoomIds(longitude, latitude);
        return roomIds.stream().map(
                roomId->(
                        // should always exist
                    RoomResponseDto.from(roomRepository.findById(roomId).get())
                )
        ).toList();
    }


    public void closeRoom(String roomId, User user){
        Optional<Room> opRoom = roomRepository.findById(roomId);
        if(opRoom.isEmpty()) throw new RuntimeException("Room dosnt exist");
        Room room= opRoom.get();
        if(!Objects.equals(room.getCreatorId(), user.getId())) throw new RuntimeException("User is not the owner of room");
        if(room.getRoomStatus().equals(RoomStatus.CLOSED)) return ;
        room.setRoomStatus(RoomStatus.CLOSED);
        room.setClosedOn(Instant.now());
        roomRepository.save(room);
        // redis work
        redisGeoRoomService.removeRoom(roomId);
    }

    // get Messages by Room
    public List<MessageResponseDto> getMessagesByRoomId(String roomId){
        List<Message> messages= messageRepository.findByRoomIdOrderByCreatedOnAsc(roomId);
        return messages.stream()
                .map(MessageResponseDto::from).toList();
    }
}
