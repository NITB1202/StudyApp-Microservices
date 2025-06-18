package com.study.chatservice.service.impl;

import com.study.chatservice.dto.request.MarkMessagesAsReadRequestDto;
import com.study.chatservice.dto.request.SendMessageRequestDto;
import com.study.chatservice.dto.request.UpdateMessageRequestDto;
import com.study.chatservice.dto.response.DeleteMessageResponseDto;
import com.study.chatservice.dto.response.MarkMessageAsReadResponseDto;
import com.study.chatservice.dto.response.MessageResponseDto;
import com.study.chatservice.dto.response.UpdateMessageResponseDto;
import com.study.chatservice.grpc.TeamServiceGrpcClient;
import com.study.chatservice.service.ChatNotificationService;
import com.study.chatservice.service.ChatService;
import com.study.chatservice.service.MessageService;
import com.study.chatservice.websocket.ChatWebSocketHandler;
import com.study.teamservice.grpc.AllTeamMembersResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
    public void sendMessage(UUID userId, UUID teamId, SendMessageRequestDto dto) {
        MessageResponseDto savedMessage = messageService.saveMessage(userId, teamId, dto);
        handler.sendMessageToOnlineMembers(teamId, SEND_TYPE, savedMessage);
        chatNotificationService.increaseNewMessageCount(teamId);
    }

    @Override
    public void sendImageMessage(UUID userId, UUID teamId, MultipartFile file) {
        MessageResponseDto savedMessage = messageService.saveImageMessage(userId, teamId, file);
        handler.sendMessageToOnlineMembers(teamId, SEND_TYPE, savedMessage);
        chatNotificationService.increaseNewMessageCount(teamId);
    }

    @Override
    public void updateMessage(UUID userId, UUID messageId, UpdateMessageRequestDto dto) {
        UUID teamId = messageService.getTeamId(messageId);
        UpdateMessageResponseDto updatedMessage = messageService.updateMessage(userId, messageId, dto);
        handler.sendMessageToOnlineMembers(teamId, UPDATE_TYPE, updatedMessage);
    }

    @Override
    public void markMessagesAsRead(UUID userId, UUID teamId, MarkMessagesAsReadRequestDto dto) {
        MarkMessageAsReadResponseDto markedMessages = messageService.markMessagesAsRead(userId, teamId, dto);
        handler.sendMessageToOnlineMembers(teamId, MARK_TYPE, markedMessages);
    }

    @Override
    public void deleteMessage(UUID userId, UUID messageId) {
        UUID teamId = messageService.getTeamId(messageId);
        DeleteMessageResponseDto deletedMessage = messageService.deleteMessage(userId, messageId);
        handler.sendMessageToOnlineMembers(teamId, DELETE_TYPE, deletedMessage);
    }
}
