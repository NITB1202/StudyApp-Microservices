package com.study.userservice.service;

import com.study.common.enums.Gender;
import com.study.userservice.controller.UserService;
import com.study.userservice.enity.User;
import com.study.userservice.grpc.*;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@GrpcService
@RequiredArgsConstructor
public class UserController extends UserServiceGrpc.UserServiceImplBase{
    private final UserService userService;

    @Override
    public void createUser(CreateUserRequest request, StreamObserver<UserResponse> responseObserver){
        User user = userService.createUser(request);
        UserResponse response = buildUserResponse(user);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getUserById(GetUserByIdRequest request, StreamObserver<UserResponse> responseObserver){
        User user = userService.getUserById(request);
        UserResponse response = buildUserResponse(user);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getUsersByListOfIds(GetUsersByListOfIdsRequest request, StreamObserver<GetUsersByListOfIdsResponse> responseObserver){
        List<UUID> ids = request.getIdsList().stream()
                .map(UUID::fromString)
                .toList();
        UUID cursor = request.getCursor().isBlank() ? null : UUID.fromString(request.getCursor());
        int size = request.getSize() > 0 ? request.getSize() : 10;

        List<User> users = userService.getUsersByListOfIds(ids, cursor, size);

        List<UserResponse> userResponses = users.stream()
                .map(this::buildUserResponse)
                .toList();

        // Determine next cursor
        String nextCursor = !users.isEmpty() && users.size() == size ? users.get(users.size() - 1).getId().toString() : "";

        GetUsersByListOfIdsResponse response = GetUsersByListOfIdsResponse.newBuilder()
                .addAllUsers(userResponses)
                .setTotal(userResponses.size())
                .setSize(size)
                .setNextCursor(nextCursor)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void searchUserByUsername(SearchUserRequest request, StreamObserver<SearchUserResponse> responseObserver){
        List<User> users = userService.searchUsersByUsername(request);

        List<UserResponse> userResponses = users.stream()
                .map(this::buildUserResponse)
                .toList();

        long total = userService.countUsersByUsername(request.getKeyword());
        String nextCursor = users.isEmpty() ? "" : users.get(users.size() - 1).getId().toString();

        SearchUserResponse response = SearchUserResponse.newBuilder()
                .addAllUsers(userResponses)
                .setTotal(total)
                .setNextCursor(nextCursor)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void updateUser(UpdateUserRequest request, StreamObserver<UserResponse> responseObserver){
        User user = userService.updateUser(request);
        UserResponse response = buildUserResponse(user);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private com.study.userservice.grpc.Gender enumToProtoEnum(Gender genderEnum){
        return switch(genderEnum) {
            case MALE -> com.study.userservice.grpc.Gender.MALE;
            case FEMALE -> com.study.userservice.grpc.Gender.FEMALE;
            default -> com.study.userservice.grpc.Gender.OTHER;
        };
    }

    private UserResponse buildUserResponse(User user){
        return UserResponse.newBuilder()
                .setId(user.getId().toString())
                .setUsername(user.getUsername())
                .setDateOfBirth(user.getDateOfBirth().toString())
                .setGender(enumToProtoEnum(user.getGender()))
                .setAvatarUrl(Objects.requireNonNullElse(user.getAvatarUrl(), ""))
                .build();
    }
}
