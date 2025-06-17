package com.study.chatservice.service.impl;

import com.study.chatservice.event.ChatEventPublisher;
import com.study.chatservice.grpc.TeamServiceGrpcClient;
import com.study.chatservice.service.ChatNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatNotificationServiceImpl implements ChatNotificationService {
    private TeamServiceGrpcClient teamClient;
    private ChatEventPublisher publisher;
    //Handle with redis;

    @Override
    public void publishChatNotification(UUID teamId, int newMessageCount) {

    }
}
