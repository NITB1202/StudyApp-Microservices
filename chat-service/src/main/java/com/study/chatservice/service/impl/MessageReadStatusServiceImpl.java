package com.study.chatservice.service.impl;

import com.study.chatservice.entity.MessageReadStatus;
import com.study.chatservice.repository.MessageReadStatusRepository;
import com.study.chatservice.service.MessageReadStatusService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageReadStatusServiceImpl implements MessageReadStatusService {
    private final MessageReadStatusRepository statusRepository;

    @Override
    public void markMessagesAsRead(UUID userId, List<UUID> messageIds) {
        List<MessageReadStatus> statuses = new ArrayList<>();

        for(UUID messageId : messageIds) {
            if(statusRepository.existsByUserIdAndMessageId(userId, messageId)) return;

            MessageReadStatus status = MessageReadStatus.builder()
                    .userId(userId)
                    .messageId(messageId)
                    .readAt(LocalDateTime.now())
                    .build();

            statuses.add(status);
        }

        statusRepository.saveAll(statuses);
    }

    @Override
    public List<UUID> getReadByList(UUID messageId) {
        List<MessageReadStatus> read = statusRepository.findByMessageId(messageId);
        return read.stream().map(MessageReadStatus::getUserId).toList();
    }

    @Override
    @Transactional
    public void deleteAllReadStatus(UUID messageId) {
        statusRepository.deleteAllByMessageId(messageId);
    }
}
