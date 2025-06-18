package com.study.apigateway.grpc;

import com.google.protobuf.ByteString;
import com.study.apigateway.dto.Chat.request.MarkMessagesAsReadRequestDto;
import com.study.apigateway.dto.Chat.request.SendMessageRequestDto;
import com.study.apigateway.dto.Chat.request.UpdateMessageRequestDto;
import com.study.chatservice.grpc.*;
import com.study.common.grpc.ActionResponse;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ChatServiceGrpcClient {
    @GrpcClient("chat-service")
    private ChatServiceGrpc.ChatServiceBlockingStub blockingStub;

    public ActionResponse sendMessage(UUID userId, UUID teamId, SendMessageRequestDto dto) {
        SendMessageRequest request = SendMessageRequest.newBuilder()
                .setUserId(userId.toString())
                .setTeamId(teamId.toString())
                .setContent(dto.getContent())
                .build();

        return blockingStub.sendMessage(request);
    }

    public ActionResponse sendImageMessage(UUID userId, UUID teamId, byte[] file) {
        ByteString byteString = ByteString.copyFrom(file);

        SendImageMessageRequest request = SendImageMessageRequest.newBuilder()
                .setUserId(userId.toString())
                .setTeamId(teamId.toString())
                .setFile(byteString)
                .build();

        return blockingStub.sendImageMessage(request);
    }

    public GetUnreadMessageCountResponse getUnreadMessageCount(UUID userId, UUID teamId) {
        GetUnreadMessageCountRequest request = GetUnreadMessageCountRequest.newBuilder()
                .setUserId(userId.toString())
                .setTeamId(teamId.toString())
                .build();

        return blockingStub.getUnreadMessageCount(request);
    }

    public MessagesResponse getMessages(UUID teamId, LocalDateTime cursor, int size) {
        String cursorStr = cursor != null ? cursor.toString() : "";

        GetMessagesRequest request = GetMessagesRequest.newBuilder()
                .setTeamId(teamId.toString())
                .setCursor(cursorStr)
                .setSize(size)
                .build();

        return blockingStub.getMessages(request);
    }

    public ActionResponse updateMessage(UUID userId, UUID messageId, UpdateMessageRequestDto dto) {
        UpdateMessageRequest request = UpdateMessageRequest.newBuilder()
                .setUserId(userId.toString())
                .setMessageId(messageId.toString())
                .setContent(dto.getContent())
                .build();

        return blockingStub.updateMessage(request);
    }

    public ActionResponse markMessagesAsRead(UUID userId, UUID teamId, MarkMessagesAsReadRequestDto dto) {
        List<String> idsStr = dto.getMessageIds().stream()
                .map(UUID::toString)
                .toList();

        MarkMessagesAsReadRequest request = MarkMessagesAsReadRequest.newBuilder()
                .setUserId(userId.toString())
                .setTeamId(teamId.toString())
                .addAllMessageIds(idsStr)
                .build();

        return blockingStub.markMessagesAsRead(request);
    }

    public ActionResponse deleteMessage(UUID userId, UUID messageId) {
        DeleteMessageRequest request = DeleteMessageRequest.newBuilder()
                .setUserId(userId.toString())
                .setMessageId(messageId.toString())
                .build();

        return blockingStub.deleteMessage(request);
    }
}
