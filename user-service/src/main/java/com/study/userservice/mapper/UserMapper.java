package com.study.userservice.mapper;

import com.study.common.mappers.GenderMapper;
import com.study.userservice.enity.User;
import com.study.userservice.grpc.ListUserResponse;
import com.study.userservice.grpc.UserDetailResponse;
import com.study.userservice.grpc.UserResponse;
import com.study.userservice.grpc.UserSummaryResponse;

import java.util.List;

public class UserMapper {
    //Private constructor to prevent initializing object
    private UserMapper() {}

    public static UserResponse toUserResponse(User user){
        String handledDateOfBirth = user.getDateOfBirth() != null ? user.getDateOfBirth().toString() : "";

        return UserResponse.newBuilder()
                .setId(user.getId().toString())
                .setUsername(user.getUsername())
                .setDateOfBirth(handledDateOfBirth)
                .setGender(GenderMapper.toProtoEnum(user.getGender()))
                .build();
    }

    public static UserDetailResponse toUserDetailResponse(User user){
        String handledDateOfBirth = user.getDateOfBirth() != null ? user.getDateOfBirth().toString() : "";

        return UserDetailResponse.newBuilder()
                .setId(user.getId().toString())
                .setUsername(user.getUsername())
                .setDateOfBirth(handledDateOfBirth)
                .setGender(GenderMapper.toProtoEnum(user.getGender()))
                .setAvatarUrl(user.getAvatarUrl())
                .build();
    }

    public static UserSummaryResponse toUserSummaryResponse(User user){
        return UserSummaryResponse.newBuilder()
                .setId(user.getId().toString())
                .setUsername(user.getUsername())
                .setAvatarUrl(user.getAvatarUrl())
                .build();
    }

    public static ListUserResponse toListUserResponse(List<User> users, long total, String nextCursor){
        List<UserSummaryResponse> userResponses = users.stream()
                .map(UserMapper::toUserSummaryResponse)
                .toList();

        return ListUserResponse.newBuilder()
                .addAllUsers(userResponses)
                .setTotal(total)
                .setNextCursor(nextCursor)
                .build();
    }
}
