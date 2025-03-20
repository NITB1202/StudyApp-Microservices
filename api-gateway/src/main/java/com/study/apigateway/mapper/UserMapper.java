package com.study.apigateway.mapper;

import com.study.apigateway.dto.User.response.UserResponseDto;
import com.study.userservice.grpc.UserResponse;

import java.time.LocalDate;
import java.util.UUID;

public class UserMapper {
    private UserMapper() {}

    public static UserResponseDto responseToResponseDto(UserResponse user) {
        return UserResponseDto.builder()
                .id(UUID.fromString(user.getId()))
                .username(user.getUsername())
                .dateOfBirth(LocalDate.parse(user.getDateOfBirth()))
                .avatarUrl(user.getAvatarUrl())
                .build();
    }
}
