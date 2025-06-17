package com.study.chatservice.service.impl;

import com.study.chatservice.entity.MessageReadStatus;
import com.study.chatservice.repository.MessageReadStatusRepository;
import com.study.chatservice.service.MessageReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageReadStatusServiceImpl implements MessageReadStatusService {
    private final MessageReadStatusRepository statusRepository;

    @Override
    public void markMessageAsRead(UUID userId, List<UUID> messageIds) {
        List<MessageReadStatus> statuses = new ArrayList<>();

        for(UUID messageId : messageIds) {
            if(statusRepository.existsByUserIdAndMessageId(userId, messageId)) return;

            MessageReadStatus status = MessageReadStatus.builder()
                    .userId(userId)
                    .messageId(messageId)
                    .build();

            statuses.add(status);
        }

        statusRepository.saveAll(statuses);
    }

    @Override
    public void deleteAllReadStatus(UUID messageId) {
        statusRepository.deleteAllByMessageId(messageId);
    }
}
