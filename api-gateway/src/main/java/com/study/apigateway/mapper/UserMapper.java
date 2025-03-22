package com.study.apigateway.mapper;

import com.study.apigateway.dto.User.response.UserResponseDto;
import com.study.userservice.grpc.UserResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class UserMapper {
    private UserMapper() {}

    public static UserResponseDto toResponseDto(UserResponse user) {
        return UserResponseDto.builder()
                .id(UUID.fromString(user.getId()))
                .username(user.getUsername())
                .dateOfBirth(LocalDate.parse(user.getDateOfBirth()))
                .gender(user.getGender())
                .avatarUrl(user.getAvatarUrl())
                .build();
    }

    public static List<UserResponseDto> toResponseDtoList(List<UserResponse> users) {
        return users.stream().map(UserMapper::toResponseDto).toList();
    }
}
