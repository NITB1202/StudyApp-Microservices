package com.study.userservice.service;

import com.study.userservice.enity.User;
import com.study.userservice.grpc.CreateUserRequest;
import com.study.userservice.grpc.UserResponse;
import com.study.userservice.grpc.UserServiceGrpc;
import com.study.userservice.repository.UserRepository;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import java.time.LocalDate;

@GrpcService
@RequiredArgsConstructor
public class UserGrpcServiceImpl extends UserServiceGrpc.UserServiceImplBase{
    private final UserRepository userRepository;

    @Override
    public void createUser(CreateUserRequest request, StreamObserver<UserResponse> responseObserver){
//        User user = User.builder()
//                .username(request.getUsername())
//                .dateOfBirth(new LocalDate(request.getDateOfBirth()))
//                .build();

    }
}
