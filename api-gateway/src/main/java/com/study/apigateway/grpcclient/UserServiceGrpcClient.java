package com.study.apigateway.grpcclient;

import com.study.apigateway.dto.User.request.CreateUserRequestDto;
import com.study.apigateway.dto.User.request.UpdateUserRequestDto;
import com.study.common.mappers.GenderMapper;
import com.study.userservice.grpc.*;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceGrpcClient {
    @GrpcClient("user-service")
    private UserServiceGrpc.UserServiceBlockingStub userServiceStub;

    public UserResponse createUser(CreateUserRequestDto dto) {
        CreateUserRequest request = CreateUserRequest.newBuilder()
                .setUsername(dto.getUsername())
                .setDateOfBirth(dto.getDateOfBirth().toString())
                .setGender(GenderMapper.toProtoEnum(dto.getGender()))
                .build();

        return userServiceStub.createUser(request);
    }

    public UserResponse getUserById(UUID id) {
        GetUserByIdRequest request = GetUserByIdRequest.newBuilder()
                .setId(id.toString())
                .build();

        return userServiceStub.getUserById(request);
    }

    public GetUsersByListOfIdsResponse getUsersByListOfIds(List<UUID> ids, UUID cursor, int size) {
        // Convert UUID list to String list
        List<String> idStrings = ids.stream()
                .map(UUID::toString)
                .toList();

        // Handle cursor (nullable)
        String cursorStr = cursor != null ? cursor.toString() : "";

        GetUsersByListOfIdsRequest request = GetUsersByListOfIdsRequest.newBuilder()
                .addAllIds(idStrings)
                .setCursor(cursorStr)
                .setSize(size)
                .build();

        return userServiceStub.getUsersByListOfIds(request);
    }

    public SearchUserResponse searchUserByUsername(String keyword, UUID cursor, int size) {
        String cursorStr = cursor != null ? cursor.toString() : "";

        SearchUserRequest request = SearchUserRequest.newBuilder()
                .setKeyword(keyword)
                .setCursor(cursorStr)
                .setSize(size)
                .build();

        return userServiceStub.searchUserByUsername(request);
    }

    public UserResponse updateUser(UUID id, UpdateUserRequestDto dto) {
        //Avoid null fields
        String username = dto.getUsername() != null ? dto.getUsername() : "";
        String dateOfBirth = dto.getDateOfBirth() != null ? dto.getDateOfBirth().toString() : "";
        Gender gender = dto.getGender() != null ? GenderMapper.toProtoEnum(dto.getGender()) : Gender.UNSPECIFIED;
        String avatarUrl = dto.getAvatarUrl() != null ? dto.getAvatarUrl() : "";

        UpdateUserRequest request = UpdateUserRequest.newBuilder()
                .setId(id.toString())
                .setUsername(username)
                .setDateOfBirth(dateOfBirth)
                .setGender(gender)
                .setAvatarUrl(avatarUrl)
                .build();

        return userServiceStub.updateUser(request);
    }
}
