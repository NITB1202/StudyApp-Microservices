package com.study.chatservice.service.impl;

import com.study.chatservice.dto.SendMessageRequestDto;
import com.study.chatservice.dto.UpdateMessageRequestDto;
import com.study.chatservice.entity.Message;
import com.study.chatservice.grpc.GetMessagesRequest;
import com.study.chatservice.repository.MessageRepository;
import com.study.chatservice.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;

    @Override
    public void saveMessage(UUID userId, UUID teamId, SendMessageRequestDto dto) {

    }

    @Override
    public void saveImageMessage(UUID userId, UUID teamId, String imageUrl) {

    }

    @Override
    public List<Message> getMessages(GetMessagesRequest request) {
        return List.of();
    }

    @Override
    public void updateMessage(UUID userId, UUID messageId, UpdateMessageRequestDto dto) {

    }

    @Override
    public void deleteMessage(UUID userId, UUID messageId) {

    }
}
