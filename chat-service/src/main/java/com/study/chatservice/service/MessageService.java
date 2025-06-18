package com.study.chatservice.service;

import com.study.chatservice.dto.request.MarkMessagesAsReadRequestDto;
import com.study.chatservice.dto.request.SendMessageRequestDto;
import com.study.chatservice.dto.request.UpdateMessageRequestDto;
import com.study.chatservice.dto.response.MessageResponseDto;
import com.study.chatservice.dto.response.UpdateMessageResponseDto;
import com.study.chatservice.grpc.GetMessagesRequest;
import com.study.chatservice.grpc.GetUnreadMessageCountRequest;
import com.study.chatservice.grpc.MessageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    long getUnreadMessageCount(GetUnreadMessageCountRequest request);
    List<MessageResponse> getMessages(GetMessagesRequest request);
    long countTeamMessages(UUID teamId);

    MessageResponseDto saveMessage(UUID userId, UUID teamId, SendMessageRequestDto dto);
    MessageResponseDto saveImageMessage(UUID userId, UUID teamId, MultipartFile file);
    UpdateMessageResponseDto updateMessage(UUID userId, UUID messageId, UpdateMessageRequestDto dto);
    void markMessagesAsRead(UUID userId, MarkMessagesAsReadRequestDto dto);
    void deleteMessage(UUID userId, UUID messageId);

    void deleteAllMessagesInTeam(UUID teamId);
}
