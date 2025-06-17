package com.study.chatservice.service.impl;

import com.study.chatservice.event.ChatEventPublisher;
import com.study.chatservice.service.ChatNotificationService;
import com.study.common.events.Chat.MessageSentEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatNotificationServiceImpl implements ChatNotificationService {
    private final RedisTemplate<UUID, Integer> redisTemplate;
    private final ChatEventPublisher publisher;
    private final static String SEND_TOPIC = "message-sent";

    @Override
    public void increaseNewMessageCount(UUID teamId) {
        Integer count = redisTemplate.opsForValue().get(teamId);

        if (count == null) {
            redisTemplate.opsForValue().set(teamId, 1);
        }
        else {
            redisTemplate.opsForValue().set(teamId, count + 1);
        }
    }

    @Override
    public void publishChatNotification(UUID teamId, String teamName, int newMessageCount, List<UUID> receiverIds) {
        MessageSentEvent event = MessageSentEvent.builder()
                .teamId(teamId)
                .teamName(teamName)
                .newMessageCount(newMessageCount)
                .receiverIds(receiverIds)
                .build();

        publisher.publishEvent(SEND_TOPIC, event);
        redisTemplate.delete(teamId);
    }
}
