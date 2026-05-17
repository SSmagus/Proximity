package com.saumya.chatapp.user.service;

import com.saumya.chatapp.auth.entity.AuthUser;
import com.saumya.chatapp.user.dto.UserRequestDto;
import com.saumya.chatapp.user.dto.UserResponseDto;
import com.saumya.chatapp.user.entity.User;
import com.saumya.chatapp.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void buildDefaultUser(AuthUser authUser){
        User user= User.builder()
                .tag(UUID.randomUUID().toString())
                .createdAt(authUser.getCreatedAt())
                .authUser(authUser)
                .build();
        user.setName(generateDummyName());
        userRepository.save(user);
    }

    public String generateDummyName(){
        String[] first={"Bebob", "Pepop","Cow","Horse","Cutie","Handsome"};
        String[] second={"Hunk","Punny","July","May","Broda","Mark"};
        Random random= new Random();
        String tag=
                first[random.nextInt(first.length)]+
                        second[random.nextInt(second.length)]+
                        ThreadLocalRandom.current().nextInt(1000,9999);

        return tag;
    }

    public UserResponseDto updateDetails(UserRequestDto userRequestDto, Long userId){
        Optional<User> user= userRepository.findById(userId);
        if(user.isEmpty()) throw new EntityNotFoundException("User dosnt exist");
        if(userRequestDto.getName()!=null) user.get().setName(userRequestDto.getName());
        if(userRequestDto.getDescription()!=null) user.get().setDescription(userRequestDto.getDescription());
        userRepository.save(user.get());
        return UserResponseDto.builder()
                .tag(user.get().getTag())
                .name(user.get().getName())
                .description(user.get().getDescription())
                .canChangeTag(user.get().getCanChangeTag())
                .createdAt(user.get().getCreatedAt())
                .build();
    }

    @Transactional
    public UserResponseDto updateTag(UserRequestDto userRequestDto, Long userId){
        Optional<User> user= userRepository.findById(userId);
        if(user.isEmpty()) throw new EntityNotFoundException("User dosnt exist");
        if(user.get().getCanChangeTag()==false) throw new IllegalStateException("User can only change tag once");
        String tag= userRequestDto.getTag();
        if(userRepository.findByTag(tag).isPresent()) throw new IllegalArgumentException("Tag already in use");
        user.get().setTag(tag);
        user.get().setCanChangeTag(false);
        userRepository.save(user.get());
        return UserResponseDto.builder()
                .tag(user.get().getTag())
                .name(user.get().getName())
                .description(user.get().getDescription())
                .canChangeTag(user.get().getCanChangeTag())
                .createdAt(user.get().getCreatedAt())
                .build();
    }
}
