package com.study.chatservice.service;

import com.study.chatservice.grpc.GetUnreadMessageCountRequest;

import java.util.UUID;

public interface MessageReadStatusService {
    void markMessageAsRead(UUID userId, UUID messageId);
    int getUnreadMessageCount(GetUnreadMessageCountRequest request);
    void deleteAllReadStatus(UUID messageId);
}
