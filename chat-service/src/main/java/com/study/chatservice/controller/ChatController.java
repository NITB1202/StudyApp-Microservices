package com.study.chatservice.controller;

import com.study.chatservice.grpc.*;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class ChatController extends ChatServiceGrpc.ChatServiceImplBase {

    @Override
    public void getUnreadMessageCount(GetUnreadMessageCountRequest request, StreamObserver<GetUnreadMessageCountResponse> responseObserver) {

    }

    @Override
    public void getMessages(GetMessagesRequest request, StreamObserver<MessagesResponse> responseObserver) {

    }
}
