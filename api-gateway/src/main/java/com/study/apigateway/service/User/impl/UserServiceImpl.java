package com.study.apigateway.service.User.impl;

import com.study.apigateway.dto.User.request.CreateUserRequestDto;
import com.study.apigateway.dto.User.request.UpdateUserRequestDto;
import com.study.apigateway.dto.User.response.ListUserResponseDto;
import com.study.apigateway.dto.User.response.UserDetailResponseDto;
import com.study.apigateway.dto.User.response.UserResponseDto;
import com.study.apigateway.grpc.UserServiceGrpcClient;
import com.study.apigateway.mapper.UserMapper;
import com.study.apigateway.service.User.UserService;
import com.study.userservice.grpc.ListUserResponse;
import com.study.userservice.grpc.UserDetailResponse;
import com.study.userservice.grpc.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserServiceGrpcClient userServiceGrpcClient;

    @Override
    public Mono<UserResponseDto> createUser(CreateUserRequestDto request) {
        return Mono.fromCallable(() -> {
            UserResponse user = userServiceGrpcClient.createUser(request);
            return UserMapper.toUserResponseDto(user);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<UserDetailResponseDto> getUserById(UUID id) {
        return Mono.fromCallable(() -> {
            UserDetailResponse user = userServiceGrpcClient.getUserById(id);
            return UserMapper.toUserDetailResponseDto(user);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<ListUserResponseDto> searchUserByUsername(String keyword, UUID cursor, int size) {
        return Mono.fromCallable(() -> {
            ListUserResponse response = userServiceGrpcClient.searchUserByUsername(keyword, cursor, size);
            return UserMapper.toListUserResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<UserResponseDto> updateUser(UUID id, UpdateUserRequestDto request){
        return Mono.fromCallable(() -> {
            UserResponse user = userServiceGrpcClient.updateUser(id, request);
            return UserMapper.toUserResponseDto(user);
        }).subscribeOn(Schedulers.boundedElastic());
    }
}