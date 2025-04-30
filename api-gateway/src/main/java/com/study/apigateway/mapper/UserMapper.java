package com.study.apigateway.mapper;

import com.study.apigateway.dto.User.response.ListUserResponseDto;
import com.study.apigateway.dto.User.response.UserDetailResponseDto;
import com.study.apigateway.dto.User.response.UserResponseDto;
import com.study.apigateway.dto.User.response.UserSummaryResponseDto;
import com.study.userservice.grpc.ListUserResponse;
import com.study.userservice.grpc.UserDetailResponse;
import com.study.userservice.grpc.UserResponse;
import com.study.userservice.grpc.UserSummaryResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class UserMapper {
    private UserMapper() {}

    public static UserResponseDto toUserResponseDto(UserResponse user) {
        return UserResponseDto.builder()
                .id(UUID.fromString(user.getId()))
                .username(user.getUsername())
                .dateOfBirth(LocalDate.parse(user.getDateOfBirth()))
                .gender(user.getGender())
                .build();
    }

    public static UserDetailResponseDto toUserDetailResponseDto(UserDetailResponse user) {
        return UserDetailResponseDto.builder()
                .id(UUID.fromString(user.getId()))
                .username(user.getUsername())
                .dateOfBirth(LocalDate.parse(user.getDateOfBirth()))
                .gender(user.getGender())
                .avatarUrl(user.getAvatarUrl())
                .build();
    }

    public static UserSummaryResponseDto toUserSummaryResponseDto(UserSummaryResponse user) {
        return UserSummaryResponseDto.builder()
                .id(UUID.fromString(user.getId()))
                .username(user.getUsername())
                .avatarUrl(user.getAvatarUrl())
                .build();
    }

    public static ListUserResponseDto toListUserResponseDto(ListUserResponse response) {
        List<UserSummaryResponseDto> users = response.getUsersList().stream()
                .map(UserMapper::toUserSummaryResponseDto)
                .toList();
        UUID nextCursor = response.getNextCursor().isEmpty() ? null : UUID.fromString(response.getNextCursor());

        return ListUserResponseDto.builder()
                .users(users)
                .total(response.getTotal())
                .nextCursor(nextCursor)
                .build();
    }
}
