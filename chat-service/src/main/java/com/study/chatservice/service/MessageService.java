package com.study.chatservice.service;

import com.study.chatservice.dto.SendMessageRequestDto;
import com.study.chatservice.dto.UpdateMessageRequestDto;
import com.study.chatservice.entity.Message;
import com.study.chatservice.grpc.GetMessagesRequest;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    void saveMessage(UUID userId, UUID teamId, SendMessageRequestDto dto);
    void saveImageMessage(UUID userId, UUID teamId, String imageUrl);
    List<Message> getMessages(GetMessagesRequest request);
    void updateMessage(UUID userId, UUID messageId, UpdateMessageRequestDto dto);
    void deleteMessage(UUID userId, UUID messageId);
}
