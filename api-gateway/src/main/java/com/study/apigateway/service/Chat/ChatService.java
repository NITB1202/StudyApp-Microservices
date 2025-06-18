package com.study.apigateway.service.Chat;

import com.study.apigateway.dto.Action.ActionResponseDto;
import com.study.apigateway.dto.Chat.request.MarkMessagesAsReadRequestDto;
import com.study.apigateway.dto.Chat.request.SendMessageRequestDto;
import com.study.apigateway.dto.Chat.request.UpdateMessageRequestDto;
import com.study.apigateway.dto.Chat.response.MessagesResponseDto;
import com.study.apigateway.dto.Chat.response.UnreadMessageCountResponseDto;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

public interface ChatService {
    Mono<ActionResponseDto> sendMessage(UUID userId, UUID teamId, SendMessageRequestDto dto);
    Mono<ActionResponseDto> sendImageMessage(UUID userId, UUID teamId, FilePart file);
    Mono<UnreadMessageCountResponseDto> getUnreadMessageCount(UUID userId, UUID teamId);
    Mono<MessagesResponseDto> getMessages(UUID teamId, LocalDateTime cursor, int size);
    Mono<ActionResponseDto> updateMessage(UUID userId, UUID messageId, UpdateMessageRequestDto dto);
    Mono<ActionResponseDto> markMessagesAsRead(UUID userId, UUID teamId, MarkMessagesAsReadRequestDto dto);
    Mono<ActionResponseDto> deleteMessage(UUID userId, UUID messageId);
}
