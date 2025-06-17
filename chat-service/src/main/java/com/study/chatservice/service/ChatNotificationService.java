package com.study.chatservice.service;

import java.util.List;
import java.util.UUID;

public interface ChatNotificationService {
    void increaseNewMessageCount(UUID teamId);
    void publishChatNotification(UUID teamId, String teamName, int newMessageCount, List<UUID> receiverIds);
}
