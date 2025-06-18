package com.study.chatservice.service;

import com.study.chatservice.entity.ChatNotification;

import java.util.UUID;

public interface ChatNotificationService {
    void increaseNewMessageCount(UUID teamId);
    ChatNotification getChatNotificationByTeamId(UUID teamId);
    void deleteChatNotification(UUID teamId);
}
