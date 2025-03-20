package com.study.apigateway.grpcclient;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
public class UserServiceGrpcClient {
//    @GrpcClient("user-service")
//    private UserServiceGrpc.UserServiceBlockingStub userServiceStub;

//    public UserResponse getUserById(String id) {
//        GetUserByIdRequest request = GetUserByIdRequest.newBuilder()
//                .setId(id)
//                .build();
//        return userServiceStub.getUserById(request);
//    }
}
