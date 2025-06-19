package com.study.apigateway.service.User.impl;

import com.study.apigateway.dto.Action.ActionResponseDto;
import com.study.apigateway.dto.User.request.UpdateUserRequestDto;
import com.study.apigateway.dto.User.response.ListUserResponseDto;
import com.study.apigateway.dto.User.response.UserDetailResponseDto;
import com.study.apigateway.dto.User.response.UserResponseDto;
import com.study.apigateway.grpc.UserServiceGrpcClient;
import com.study.apigateway.mapper.ActionMapper;
import com.study.apigateway.mapper.UserMapper;
import com.study.apigateway.service.User.UserService;
import com.study.common.service.FileService;
import com.study.common.utils.FileUtils;
import com.study.common.exceptions.BusinessException;
import com.study.common.grpc.ActionResponse;
import com.study.userservice.grpc.ListUserResponse;
import com.study.userservice.grpc.UserDetailResponse;
import com.study.userservice.grpc.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserServiceGrpcClient userServiceGrpcClient;
    private final FileService fileService;
    private final String AVATAR_FOLDER = "users";

    @Override
    public Mono<UserResponseDto> createUser(String username, String dateOfBirth, String gender, String avatarUrl) {
        return Mono.fromCallable(() -> {
            UserResponse user = userServiceGrpcClient.createUser(username, dateOfBirth, gender, avatarUrl);
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

    @Override
    public Mono<ActionResponseDto> uploadUserAvatar(UUID id, FilePart file) {
        if(!FileUtils.isImage(file)) {
            throw new BusinessException("User's avatar must be an image.");
        }

        return DataBufferUtils.join(file.content())
                .flatMap(buffer -> {
                    byte[] bytes = new byte[buffer.readableByteCount()];
                    buffer.read(bytes);
                    DataBufferUtils.release(buffer);

                    String avatarUrl = fileService.uploadFile(AVATAR_FOLDER, id.toString(), bytes).getUrl();
                    ActionResponse response = userServiceGrpcClient.uploadUserAvatar(id, avatarUrl);

                    return Mono.fromCallable(() -> ActionMapper.toResponseDto(response))
                            .subscribeOn(Schedulers.boundedElastic());
                });
    }
}