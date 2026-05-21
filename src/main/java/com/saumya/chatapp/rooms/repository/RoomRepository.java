package com.saumya.chatapp.rooms.repository;

import com.saumya.chatapp.rooms.entity.Room;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoomRepository extends MongoRepository<Room, String> {
}
