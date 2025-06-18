package com.study.apigateway.service.Chat.impl;

import com.study.apigateway.dto.Action.ActionResponseDto;
import com.study.apigateway.dto.Chat.request.MarkMessagesAsReadRequestDto;
import com.study.apigateway.dto.Chat.request.SendMessageRequestDto;
import com.study.apigateway.dto.Chat.request.UpdateMessageRequestDto;
import com.study.apigateway.dto.Chat.response.MessagesResponseDto;
import com.study.apigateway.dto.Chat.response.UnreadMessageCountResponseDto;
import com.study.apigateway.grpc.ChatServiceGrpcClient;
import com.study.apigateway.mapper.ActionMapper;
import com.study.apigateway.mapper.MessageMapper;
import com.study.apigateway.service.Chat.ChatService;
import com.study.chatservice.grpc.GetUnreadMessageCountResponse;
import com.study.chatservice.grpc.MessagesResponse;
import com.study.common.exceptions.BusinessException;
import com.study.common.grpc.ActionResponse;
import com.study.common.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final ChatServiceGrpcClient chatClient;

    @Override
    public Mono<ActionResponseDto> sendMessage(UUID userId, UUID teamId, SendMessageRequestDto dto) {
        return Mono.fromCallable(()->{
            ActionResponse response = chatClient.sendMessage(userId, teamId, dto);
            return ActionMapper.toResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<ActionResponseDto> sendImageMessage(UUID userId, UUID teamId, FilePart file) {
        if(!FileUtils.isImage(file)) {
            throw new BusinessException("Invalid image file.");
        }

        return DataBufferUtils.join(file.content())
                .flatMap(buffer -> {
                    byte[] bytes = new byte[buffer.readableByteCount()];
                    buffer.read(bytes);
                    DataBufferUtils.release(buffer);

                    ActionResponse response = chatClient.sendImageMessage(userId, teamId, bytes);

                    return Mono.fromCallable(() -> ActionMapper.toResponseDto(response))
                            .subscribeOn(Schedulers.boundedElastic());
                });
    }

    @Override
    public Mono<UnreadMessageCountResponseDto> getUnreadMessageCount(UUID userId, UUID teamId) {
        return Mono.fromCallable(()->{
            GetUnreadMessageCountResponse response = chatClient.getUnreadMessageCount(userId, teamId);
            return MessageMapper.toUnreadMessageCountResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<MessagesResponseDto> getMessages(UUID teamId, LocalDateTime cursor, int size) {
        return Mono.fromCallable(()->{
            MessagesResponse response = chatClient.getMessages(teamId, cursor, size);
            return MessageMapper.toMessagesResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<ActionResponseDto> updateMessage(UUID userId, UUID messageId, UpdateMessageRequestDto dto) {
        return Mono.fromCallable(()->{
            ActionResponse response = chatClient.updateMessage(userId, messageId, dto);
            return ActionMapper.toResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<ActionResponseDto> markMessagesAsRead(UUID userId, UUID teamId, MarkMessagesAsReadRequestDto dto) {
        return Mono.fromCallable(()->{
            ActionResponse response = chatClient.markMessagesAsRead(userId, teamId, dto);
            return ActionMapper.toResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<ActionResponseDto> deleteMessage(UUID userId, UUID messageId) {
        return Mono.fromCallable(()->{
            ActionResponse response = chatClient.deleteMessage(userId, messageId);
            return ActionMapper.toResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
