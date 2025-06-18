package com.study.userservice.service;

import com.study.common.events.User.UserCreatedEvent;
import com.study.common.exceptions.BusinessException;
import com.study.common.exceptions.NotFoundException;
import com.study.common.mappers.GenderMapper;
import com.study.userservice.enity.User;
import com.study.userservice.event.UserEventPublisher;
import com.study.userservice.grpc.*;
import com.study.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserEventPublisher publisher;
    private final static String CREATE_TOPIC = "user-created";

    @Override
    public User createUser(CreateUserRequest request) {
        if(userRepository.existsByUsernameIgnoreCase(request.getUsername()))
            throw new BusinessException("Username already exists");

        LocalDate dateOfBirth = request.getDateOfBirth().isEmpty() ? null : LocalDate.parse(request.getDateOfBirth());

        User user = User.builder()
                .username(request.getUsername())
                .dateOfBirth(dateOfBirth)
                .gender(GenderMapper.toEnum(request.getGender()))
                .avatarUrl(request.getAvatarUrl())
                .build();

        userRepository.save(user);

        UserCreatedEvent event = UserCreatedEvent.builder()
                .userId(user.getId())
                .build();

        publisher.publishEvent(CREATE_TOPIC, event);

        return user;
    }

    @Override
    public User getUserById(GetUserByIdRequest request) {
        return userRepository.findById(UUID.fromString(request.getId())).orElseThrow(
                () -> new NotFoundException("User not found")
        );
    }

    @Override
    public List<User> searchUsersByUsername(SearchUserRequest request) {
        int size = request.getSize() > 0 ? request.getSize() : 10;
        UUID cursor = request.getCursor().isEmpty() ? null : UUID.fromString(request.getCursor());

        Pageable pageable = PageRequest.of(0, size, Sort.by("id").ascending());

        return userRepository.searchByUsernameWithCursor(request.getKeyword(), cursor, pageable);
    }

    @Override
    public long countUsersByUsername(String username){
        return userRepository.countByUsername(username);
    }

    @Override
    public User updateUser(UpdateUserRequest request) {
        User user = userRepository.findById(UUID.fromString(request.getId())).orElseThrow(
                () -> new NotFoundException("User not found")
        );

        //Check if username has already existed
        if(!request.getUsername().isBlank()){
            if(userRepository.existsByUsernameIgnoreCase(request.getUsername()))
                throw new BusinessException("Username already exists");
            else
                user.setUsername(request.getUsername());
        }

        if(!request.getDateOfBirth().isBlank()){
            try {
                LocalDate dateOfBirth = LocalDate.parse(request.getDateOfBirth());
                user.setDateOfBirth(dateOfBirth);
            } catch (DateTimeParseException e) {
                throw new BusinessException("Invalid date format. Expected yyyy-MM-dd");
            }
        }

        if(request.getGender() != com.study.userservice.grpc.Gender.UNSPECIFIED)
            user.setGender(GenderMapper.toEnum(request.getGender()));

        return userRepository.save(user);
    }

    @Override
    public void uploadUserAvatar(UploadUserAvatarRequest request){
        User user = userRepository.findById(UUID.fromString(request.getId())).orElseThrow(
                () -> new NotFoundException("User not found")
        );

        if(request.getAvatarUrl().isBlank()){
            throw new BusinessException("Avatar url is empty");
        }

        user.setAvatarUrl(request.getAvatarUrl());
        userRepository.save(user);
    }
}