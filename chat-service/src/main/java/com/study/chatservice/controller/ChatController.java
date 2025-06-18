package com.study.chatservice.controller;

import com.study.chatservice.grpc.*;
import com.study.chatservice.mapper.MessageMapper;
import com.study.chatservice.service.ChatService;
import com.study.chatservice.service.MessageService;
import com.study.common.grpc.ActionResponse;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;
import java.util.UUID;

@GrpcService
@RequiredArgsConstructor
public class ChatController extends ChatServiceGrpc.ChatServiceImplBase {
    private final MessageService messageService;
    private final ChatService chatService;

    @Override
    public void sendMessage(SendMessageRequest request, StreamObserver<ActionResponse> responseObserver) {
        chatService.sendMessage(request);

        ActionResponse response = ActionResponse.newBuilder()
                .setSuccess(true)
                .setMessage("View result via websocket connection.")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void sendImageMessage(SendImageMessageRequest request, StreamObserver<ActionResponse> responseObserver) {
        chatService.sendImageMessage(request);

        ActionResponse response = ActionResponse.newBuilder()
                .setSuccess(true)
                .setMessage("View result via websocket connection.")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getUnreadMessageCount(GetUnreadMessageCountRequest request, StreamObserver<GetUnreadMessageCountResponse> responseObserver) {
        long count = messageService.getUnreadMessageCount(request);

        GetUnreadMessageCountResponse response = GetUnreadMessageCountResponse.newBuilder()
                .setCount(count)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getMessages(GetMessagesRequest request, StreamObserver<MessagesResponse> responseObserver) {
        UUID teamId = UUID.fromString(request.getTeamId());

        List<MessageResponse> messages = messageService.getMessages(request);
        long total = messageService.countTeamMessages(teamId);
        String nextCursor = !messages.isEmpty() && messages.size() == request.getSize() ?
                messages.get(messages.size() - 1).getCreatedAt() : "";

        MessagesResponse response = MessageMapper.toMessagesResponse(messages, nextCursor, total);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void updateMessage(UpdateMessageRequest request, StreamObserver<ActionResponse> responseObserver) {
        chatService.updateMessage(request);

        ActionResponse response = ActionResponse.newBuilder()
                .setSuccess(true)
                .setMessage("View result via websocket connection.")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void markMessagesAsRead(MarkMessagesAsReadRequest request, StreamObserver<ActionResponse> responseObserver) {
        chatService.markMessagesAsRead(request);

        ActionResponse response = ActionResponse.newBuilder()
                .setSuccess(true)
                .setMessage("View result via websocket connection.")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void deleteMessage(DeleteMessageRequest request, StreamObserver<ActionResponse> responseObserver) {
        chatService.deleteMessage(request);

        ActionResponse response = ActionResponse.newBuilder()
                .setSuccess(true)
                .setMessage("View result via websocket connection.")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
