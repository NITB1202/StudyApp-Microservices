package com.study.chatservice.service;

import com.study.chatservice.grpc.*;

import java.util.List;
import java.util.UUID;

public interface ChatService {
    List<UUID> getOfflineUsersInTeam(UUID teamId);
    void sendMessage(SendMessageRequest request);
    void sendImageMessage(SendImageMessageRequest request);
    void updateMessage(UpdateMessageRequest request);
    void markMessagesAsRead(MarkMessagesAsReadRequest request);
    void deleteMessage(DeleteMessageRequest request);
}
