package com.study.chatservice.service;

import java.util.List;
import java.util.UUID;

public interface MessageReadStatusService {
    void markMessagesAsRead(UUID userId, List<UUID> messageIds);
    List<UUID> getReadByList(UUID messageId);
    void deleteAllReadStatus(UUID messageId);
}
