package com.study.chatservice.mapper;

import com.study.chatservice.dto.response.DeleteMessageResponseDto;
import com.study.chatservice.dto.response.MarkMessageAsReadResponseDto;
import com.study.chatservice.dto.response.MessageResponseDto;
import com.study.chatservice.dto.response.UpdateMessageResponseDto;
import com.study.chatservice.entity.Message;
import com.study.chatservice.grpc.MessageResponse;
import com.study.chatservice.grpc.MessagesResponse;
import com.study.userservice.grpc.UserDetailResponse;

import java.util.List;
import java.util.UUID;

public class MessageMapper {
    private MessageMapper() {}

    public static MessageResponseDto toMessageResponseDto(Message message, UserDetailResponse user) {
        String content = message.getContent() != null ? message.getContent() : "";
        String imageUrl = message.getImageUrl() != null ? message.getImageUrl() : "";

        return MessageResponseDto.builder()
                .id(message.getId())
                .username(user.getUsername())
                .avatarUrl(user.getAvatarUrl())
                .content(content)
                .imageUrl(imageUrl)
                .createdAt(message.getCreatedAt())
                .isDeleted(message.getIsDeleted())
                .build();
    }

    public static UpdateMessageResponseDto toUpdateMessageResponseDto(Message message) {
        return UpdateMessageResponseDto.builder()
                .id(message.getId())
                .content(message.getContent())
                .build();
    }

    public static MessageResponse toMessageResponse(Message message, UserDetailResponse user, List<String> readBy) {
        String content = message.getContent() != null ? message.getContent() : "";
        String imageUrl = message.getImageUrl() != null ? message.getImageUrl() : "";

        return MessageResponse.newBuilder()
                .setId(message.getId().toString())
                .setUsername(user.getUsername())
                .setAvatarUrl(user.getAvatarUrl())
                .setContent(content)
                .setCreatedAt(message.getCreatedAt().toString())
                .setImageUrl(imageUrl)
                .setIsDeleted(message.getIsDeleted())
                .addAllReadBy(readBy)
                .build();
    }

    public static MessagesResponse toMessagesResponse(List<MessageResponse> messages, String nextCursor, long total) {
        return MessagesResponse.newBuilder()
                .addAllMessages(messages)
                .setNextCursor(nextCursor)
                .setTotal(total)
                .build();
    }

    public static MarkMessageAsReadResponseDto toMarkMessageAsReadResponseDto(UUID userId, List<UUID> messageIds) {
        return MarkMessageAsReadResponseDto.builder()
                .userId(userId)
                .messageIds(messageIds)
                .build();
    }

    public static DeleteMessageResponseDto toDeleteMessageResponseDto(Message message) {
        return DeleteMessageResponseDto.builder()
                .messageId(message.getId())
                .build();
    }
}
