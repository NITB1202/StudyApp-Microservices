package com.study.chatservice.service;

import com.study.chatservice.dto.response.DeleteMessageResponseDto;
import com.study.chatservice.dto.response.MarkMessageAsReadResponseDto;
import com.study.chatservice.dto.response.MessageResponseDto;
import com.study.chatservice.dto.response.UpdateMessageResponseDto;
import com.study.chatservice.grpc.GetMessagesRequest;
import com.study.chatservice.grpc.GetUnreadMessageCountRequest;
import com.study.chatservice.grpc.MessageResponse;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    long getUnreadMessageCount(GetUnreadMessageCountRequest request);
    List<MessageResponse> getMessages(GetMessagesRequest request);
    long countTeamMessages(UUID teamId);

    MessageResponseDto saveMessage(UUID userId, UUID teamId, String content);
    MessageResponseDto saveImageMessage(UUID userId, UUID teamId, byte[] bytes);
    UpdateMessageResponseDto updateMessage(UUID userId, UUID messageId, String content);
    MarkMessageAsReadResponseDto markMessagesAsRead(UUID userId, UUID teamId, List<UUID> messageIds);
    DeleteMessageResponseDto deleteMessage(UUID userId, UUID messageId);

    UUID getTeamId(UUID messageId);
    void deleteAllMessagesInTeam(UUID teamId);
}
