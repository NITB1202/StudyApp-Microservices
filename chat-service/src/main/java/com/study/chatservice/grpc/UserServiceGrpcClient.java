package com.study.chatservice.grpc;

import com.study.userservice.grpc.GetUserByIdRequest;
import com.study.userservice.grpc.UserDetailResponse;
import com.study.userservice.grpc.UserServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceGrpcClient {
    @GrpcClient("user-service")
    private UserServiceGrpc.UserServiceBlockingStub blockingStub;

    public UserDetailResponse getUserById(UUID id) {
        GetUserByIdRequest request = GetUserByIdRequest.newBuilder()
                .setId(id.toString())
                .build();

        return blockingStub.getUserById(request);
    }
}
