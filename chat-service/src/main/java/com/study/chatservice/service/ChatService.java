package com.study.chatservice.service;

import com.study.chatservice.dto.request.MarkMessagesAsReadRequestDto;
import com.study.chatservice.dto.request.SendMessageRequestDto;
import com.study.chatservice.dto.request.UpdateMessageRequestDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface ChatService {
    List<UUID> getOfflineUsersInTeam(UUID teamId);
    void sendMessage(UUID userId, UUID teamId, SendMessageRequestDto dto);
    void sendImageMessage(UUID userId, UUID teamId, MultipartFile file);
    void updateMessage(UUID userId, UUID messageId, UpdateMessageRequestDto dto);
    void markMessagesAsRead(UUID userId, MarkMessagesAsReadRequestDto dto);
    void deleteMessage(UUID userId, UUID messageId);
}
