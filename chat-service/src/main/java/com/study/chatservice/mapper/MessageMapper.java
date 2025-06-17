package com.study.chatservice.mapper;

import com.study.chatservice.dto.response.MessageResponseDto;
import com.study.chatservice.dto.response.UpdateMessageResponseDto;
import com.study.chatservice.entity.Message;
import com.study.userservice.grpc.UserDetailResponse;

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
}
