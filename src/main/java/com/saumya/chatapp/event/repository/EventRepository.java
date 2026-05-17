package com.saumya.chatapp.event.repository;

import com.saumya.chatapp.event.entity.Event;
import com.saumya.chatapp.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllEventsByCreator(User creator);
}
