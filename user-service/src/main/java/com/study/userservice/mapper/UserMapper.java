package com.study.userservice.mapper;

import com.study.common.mappers.GenderMapper;
import com.study.userservice.enity.User;
import com.study.userservice.grpc.UserResponse;

import java.util.List;
import java.util.Objects;

public class UserMapper {
    //Private constructor to prevent initializing object
    private UserMapper() {}

    public static UserResponse toUserResponse(User user){
        return UserResponse.newBuilder()
                .setId(user.getId().toString())
                .setUsername(user.getUsername())
                .setDateOfBirth(user.getDateOfBirth().toString())
                .setGender(GenderMapper.toProtoEnum(user.getGender()))
                .setAvatarUrl(Objects.requireNonNullElse(user.getAvatarUrl(), ""))
                .build();
    }

    public static List<UserResponse> toUserResponseList(List<User> users){
        return users.stream()
                .map(UserMapper::toUserResponse)
                .toList();
    }
}
