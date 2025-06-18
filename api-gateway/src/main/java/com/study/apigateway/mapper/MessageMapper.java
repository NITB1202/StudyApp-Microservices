package com.study.apigateway.mapper;

import com.study.apigateway.dto.Chat.response.MessageResponseDto;
import com.study.apigateway.dto.Chat.response.MessagesResponseDto;
import com.study.apigateway.dto.Chat.response.UnreadMessageCountResponseDto;
import com.study.chatservice.grpc.GetUnreadMessageCountResponse;
import com.study.chatservice.grpc.MessageResponse;
import com.study.chatservice.grpc.MessagesResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class MessageMapper {
    private MessageMapper() {}

    public static UnreadMessageCountResponseDto toUnreadMessageCountResponseDto(GetUnreadMessageCountResponse message) {
        return UnreadMessageCountResponseDto.builder()
                .count(message.getCount())
                .build();
    }

    public static MessageResponseDto toMessageResponseDto(MessageResponse message) {
        return MessageResponseDto.builder()
                .id(UUID.fromString(message.getId()))
                .username(message.getUsername())
                .avatarUrl(message.getAvatarUrl())
                .content(message.getContent())
                .createdAt(LocalDateTime.parse(message.getCreatedAt()))
                .imageUrl(message.getImageUrl())
                .readBy(message.getReadByList())
                .isDeleted(message.getIsDeleted())
                .build();
    }

    public static MessagesResponseDto toMessagesResponseDto(MessagesResponse messages) {
        LocalDateTime nextCursor = messages.getNextCursor().isEmpty() ? null : LocalDateTime.parse(messages.getNextCursor());

        List<MessageResponseDto> dto = messages.getMessagesList().stream()
                .map(MessageMapper::toMessageResponseDto)
                .toList();

        return MessagesResponseDto.builder()
                .messages(dto)
                .total(messages.getTotal())
                .nextCursor(nextCursor)
                .build();
    }
}
