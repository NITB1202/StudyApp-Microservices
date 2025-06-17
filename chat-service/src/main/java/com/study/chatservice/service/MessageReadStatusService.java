package com.study.chatservice.service;

import java.util.List;
import java.util.UUID;

public interface MessageReadStatusService {
    void markMessageAsRead(UUID userId, List<UUID> messageIds);
    void deleteAllReadStatus(UUID messageId);
}
