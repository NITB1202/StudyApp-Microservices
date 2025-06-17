package com.study.chatservice.service;

import java.util.UUID;

public interface ChatNotificationService {
    void publishChatNotification(UUID teamId, int newMessageCount);
}
