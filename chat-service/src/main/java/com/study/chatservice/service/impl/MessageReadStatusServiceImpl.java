package com.study.chatservice.service.impl;

import com.study.chatservice.grpc.GetUnreadMessageCountRequest;
import com.study.chatservice.repository.MessageReadStatusRepository;
import com.study.chatservice.service.MessageReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageReadStatusServiceImpl implements MessageReadStatusService {
    private final MessageReadStatusRepository messageReadStatusRepository;

    @Override
    public void markMessageAsRead(UUID userId, UUID messageId) {

    }

    @Override
    public int getUnreadMessageCount(GetUnreadMessageCountRequest request) {
        return 0;
    }

    @Override
    public void deleteAllReadStatus(UUID messageId) {

    }
}
