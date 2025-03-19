package com.study.userservice.service;

import com.study.common.enums.Gender;
import com.study.userservice.controller.UserService;
import com.study.userservice.enity.User;
import com.study.userservice.grpc.*;
import com.study.userservice.mapper.GenderMapper;
import com.study.userservice.mapper.UserMapper;
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
        UserResponse response = UserMapper.toUserResponse(user);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getUserById(GetUserByIdRequest request, StreamObserver<UserResponse> responseObserver){
        User user = userService.getUserById(request);
        UserResponse response = UserMapper.toUserResponse(user);
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
        List<UserResponse> userResponses = UserMapper.toUserResponseList(users);

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
        List<UserResponse> userResponses = UserMapper.toUserResponseList(users);

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
        UserResponse response = UserMapper.toUserResponse(user);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
