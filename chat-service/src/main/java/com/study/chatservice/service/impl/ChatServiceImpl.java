package com.study.chatservice.service.impl;

import com.study.chatservice.dto.response.DeleteMessageResponseDto;
import com.study.chatservice.dto.response.MarkMessageAsReadResponseDto;
import com.study.chatservice.dto.response.MessageResponseDto;
import com.study.chatservice.dto.response.UpdateMessageResponseDto;
import com.study.chatservice.grpc.*;
import com.study.chatservice.service.ChatNotificationService;
import com.study.chatservice.service.ChatService;
import com.study.chatservice.service.MessageService;
import com.study.chatservice.websocket.ChatWebSocketHandler;
import com.study.teamservice.grpc.AllTeamMembersResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final MessageService messageService;
    private final ChatNotificationService chatNotificationService;
    private final ChatWebSocketHandler handler;
    private final TeamServiceGrpcClient teamClient;

    private final static String SEND_TYPE = "new";
    private final static String UPDATE_TYPE = "update";
    private final static String MARK_TYPE = "mark";
    private final static String DELETE_TYPE = "delete";

    @Override
    public List<UUID> getOfflineUsersInTeam(UUID teamId) {
        AllTeamMembersResponse members = teamClient.getAllTeamMembers(teamId);
        List<UUID> memberIds = members.getMembersList().stream()
                .map(m -> UUID.fromString(m.getUserId()))
                .toList();

        List<UUID> offlineMembers = new ArrayList<>();

        for(UUID memberId : memberIds) {
           if(!handler.isUserInTeam(memberId, teamId)){
                offlineMembers.add(memberId);
           }
        }

        return offlineMembers;
    }

    @Override
    public void sendMessage(SendMessageRequest request) {
        UUID userId = UUID.fromString(request.getUserId());
        UUID teamId = UUID.fromString(request.getTeamId());
        MessageResponseDto savedMessage = messageService.saveMessage(userId, teamId, request.getContent());
        handler.sendMessageToOnlineMembers(teamId, SEND_TYPE, savedMessage);
        chatNotificationService.increaseNewMessageCount(teamId);
    }

    @Override
    public void sendImageMessage(SendImageMessageRequest request) {
        UUID userId = UUID.fromString(request.getUserId());
        UUID teamId = UUID.fromString(request.getTeamId());
        byte[] bytes = request.getFile().toByteArray();
        MessageResponseDto savedMessage = messageService.saveImageMessage(userId, teamId, bytes);
        handler.sendMessageToOnlineMembers(teamId, SEND_TYPE, savedMessage);
        chatNotificationService.increaseNewMessageCount(teamId);
    }

    @Override
    public void updateMessage(UpdateMessageRequest request) {
        UUID userId = UUID.fromString(request.getUserId());
        UUID messageId = UUID.fromString(request.getMessageId());
        UUID teamId = messageService.getTeamId(messageId);
        UpdateMessageResponseDto updatedMessage = messageService.updateMessage(userId, messageId, request.getContent());
        handler.sendMessageToOnlineMembers(teamId, UPDATE_TYPE, updatedMessage);
    }

    @Override
    public void markMessagesAsRead(MarkMessagesAsReadRequest request) {
        UUID userId = UUID.fromString(request.getUserId());
        UUID teamId = UUID.fromString(request.getTeamId());
        List<UUID> messageIds = request.getMessageIdsList().stream().map(UUID::fromString).toList();
        MarkMessageAsReadResponseDto markedMessages = messageService.markMessagesAsRead(userId, teamId, messageIds);
        handler.sendMessageToOnlineMembers(teamId, MARK_TYPE, markedMessages);
    }

    @Override
    public void deleteMessage(DeleteMessageRequest request) {
        UUID userId = UUID.fromString(request.getUserId());
        UUID messageId = UUID.fromString(request.getMessageId());
        UUID teamId = messageService.getTeamId(messageId);
        DeleteMessageResponseDto deletedMessage = messageService.deleteMessage(userId, messageId);
        handler.sendMessageToOnlineMembers(teamId, DELETE_TYPE, deletedMessage);
    }
}
