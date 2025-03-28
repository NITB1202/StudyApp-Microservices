package com.study.userservice.controller;

import com.study.userservice.enity.User;
import com.study.userservice.grpc.*;
import com.study.userservice.mapper.UserMapper;
import com.study.userservice.service.UserService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;

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
    public void getUsersByListOfIds(GetUsersByListOfIdsRequest request, StreamObserver<ListUserResponse> responseObserver){
        List<User> users = userService.getUsersByListOfIds(request);
        List<UserResponse> userResponses = UserMapper.toUserResponseList(users);

        // Determine next cursor
        String nextCursor = !users.isEmpty() && users.size() == request.getSize() ? users.get(users.size() - 1).getId().toString() : "";

        ListUserResponse response = ListUserResponse.newBuilder()
                .addAllUsers(userResponses)
                .setTotal(request.getIdsCount())
                .setNextCursor(nextCursor)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void searchUserByUsername(SearchUserRequest request, StreamObserver<ListUserResponse> responseObserver){
        List<User> users = userService.searchUsersByUsername(request);
        List<UserResponse> userResponses = UserMapper.toUserResponseList(users);

        long total = userService.countUsersByUsername(request.getKeyword());
        String nextCursor = !users.isEmpty() && users.size() == request.getSize() ? users.get(users.size() - 1).getId().toString() : "";

        ListUserResponse response = ListUserResponse.newBuilder()
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
