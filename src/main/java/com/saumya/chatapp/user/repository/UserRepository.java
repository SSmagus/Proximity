package com.saumya.chatapp.user.repository;

import com.saumya.chatapp.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // need to update methods, AuthUserId not needed anymore
    Optional<User> findByAuthUserId(Long id);
    Optional<User> findByTag(String tag);
}
