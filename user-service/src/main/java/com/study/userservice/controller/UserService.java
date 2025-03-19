package com.study.userservice.controller;

import com.study.common.enums.Gender;
import com.study.common.exceptions.BusinessException;
import com.study.common.exceptions.NotFoundException;
import com.study.userservice.enity.User;
import com.study.userservice.grpc.CreateUserRequest;
import com.study.userservice.grpc.GetUserByIdRequest;
import com.study.userservice.grpc.SearchUserRequest;
import com.study.userservice.grpc.UpdateUserRequest;
import com.study.userservice.mapper.GenderMapper;
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
public class UserService {
    private final UserRepository userRepository;

    public User createUser(CreateUserRequest request) {
        if(userRepository.existsByUsernameIgnoreCase(request.getUsername()))
            throw new BusinessException("Username already exists");

        LocalDate dateOfBirth;
        try {
            dateOfBirth = LocalDate.parse(request.getDateOfBirth());
        } catch (DateTimeParseException e) {
            throw new BusinessException("Invalid date format. Expected yyyy-MM-dd");
        }

        User user = User.builder()
                .username(request.getUsername())
                .dateOfBirth(dateOfBirth)
                .gender(GenderMapper.toEnum(request.getGender()))
                .build();

        return userRepository.save(user);
    }

    public User getUserById(GetUserByIdRequest request) {
        return userRepository.findById(UUID.fromString(request.getId())).orElseThrow(
                () -> new NotFoundException("User not found")
        );
    }

    public List<User> getUsersByListOfIds(List<UUID> ids, UUID cursor, int size){
        Pageable pageable = PageRequest.of(0, size, Sort.by("id").ascending());
        return userRepository.findByIdsWithCursor(ids, cursor, pageable);
    }

    public List<User> searchUsersByUsername(SearchUserRequest request) {
        int size = request.getSize() > 0 ? request.getSize() : 10;
        UUID cursor = request.getCursor().isEmpty() ? null : UUID.fromString(request.getCursor());

        Pageable pageable = PageRequest.of(0, size, Sort.by("id").ascending());

        return userRepository.searchByUsernameWithCursor(request.getKeyword(), cursor, pageable);
    }

    public long countUsersByUsername(String username){
        return userRepository.countByUsername(username);
    }

    public User updateUser(UpdateUserRequest request) {
        User user = userRepository.findById(UUID.fromString(request.getId())).orElseThrow(
                () -> new NotFoundException("User not found")
        );

        //Check if username has already existed, ignore the case username is unchanged.
        if(!request.getUsername().isBlank()){
            if(!request.getUsername().equals(user.getUsername()) &&
                    userRepository.existsByUsernameIgnoreCase(request.getUsername()))
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

        if(!request.getAvatarUrl().isBlank())
            user.setAvatarUrl(request.getAvatarUrl());

        return userRepository.save(user);
    }
}