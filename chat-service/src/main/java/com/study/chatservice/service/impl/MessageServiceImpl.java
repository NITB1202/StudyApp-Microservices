package com.study.chatservice.service.impl;

import com.study.chatservice.dto.response.DeleteMessageResponseDto;
import com.study.chatservice.dto.response.MarkMessageAsReadResponseDto;
import com.study.chatservice.dto.response.MessageResponseDto;
import com.study.chatservice.dto.response.UpdateMessageResponseDto;
import com.study.chatservice.entity.Message;
import com.study.chatservice.grpc.*;
import com.study.chatservice.mapper.MessageMapper;
import com.study.chatservice.repository.MessageRepository;
import com.study.chatservice.service.MessageReadStatusService;
import com.study.chatservice.service.MessageService;
import com.study.common.exceptions.BusinessException;
import com.study.common.exceptions.NotFoundException;
import com.study.userservice.grpc.UserDetailResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;
    private final MessageReadStatusService statusService;

    private final UserServiceGrpcClient userClient;
    private final TeamServiceGrpcClient teamClient;
    private final DocumentServiceGrpcClient documentClient;

    private static final int DEFAULT_SIZE = 10;
    private final static String FOLDER_PATH = "chats";

    @Override
    public long getUnreadMessageCount(GetUnreadMessageCountRequest request) {
        UUID userId = UUID.fromString(request.getUserId());
        UUID teamId = UUID.fromString(request.getTeamId());

        return messageRepository.countUnreadMessages(userId, teamId);
    }

    @Override
    public List<MessageResponse> getMessages(GetMessagesRequest request) {
        UUID teamId = UUID.fromString(request.getTeamId());
        int size = request.getSize() > 0 ? request.getSize() : DEFAULT_SIZE;

        Pageable pageable = PageRequest.of(0, size, Sort.by("createdAt").descending());

        if(request.getCursor().isEmpty()){
            List<Message> messages = messageRepository.findByTeamId(teamId, pageable);
            return toListMessageResponse(messages);
        }

        LocalDateTime cursor = LocalDateTime.parse(request.getCursor());
        List<Message> messages = messageRepository.findByTeamIdAndCreatedAtLessThan(teamId, cursor, pageable);
        return toListMessageResponse(messages);
    }

    @Override
    public long countTeamMessages(UUID teamId) {
        return messageRepository.countByTeamId(teamId);
    }

    @Override
    public MessageResponseDto saveMessage(UUID userId, UUID teamId, String content) {
        teamClient.validateUsersInTeam(teamId, Set.of(userId));

        Message message = Message.builder()
                .userId(userId)
                .teamId(teamId)
                .content(content)
                .createdAt(LocalDateTime.now())
                .isDeleted(false)
                .build();

        messageRepository.save(message);
        UserDetailResponse user = userClient.getUserById(userId);

        return MessageMapper.toMessageResponseDto(message, user);
    }

    @Override
    public MessageResponseDto saveImageMessage(UUID userId, UUID teamId, byte[] bytes) {
        teamClient.validateUsersInTeam(teamId, Set.of(userId));

        Message message = Message.builder()
                .userId(userId)
                .teamId(teamId)
                .createdAt(LocalDateTime.now())
                .isDeleted(false)
                .build();

        messageRepository.save(message);

        //Upload image
        String folderPath = FOLDER_PATH + "/" + teamId;
        String publicId = message.getId().toString();

        String url = documentClient.uploadImage(folderPath, publicId, bytes).getUrl();
        message.setImageUrl(url);
        messageRepository.save(message);

        UserDetailResponse user = userClient.getUserById(userId);

        return MessageMapper.toMessageResponseDto(message, user);
    }

    @Override
    public UpdateMessageResponseDto updateMessage(UUID userId, UUID messageId, String content) {
        Message message = messageRepository.findById(messageId).orElseThrow(
                () -> new BusinessException("Message not found.")
        );

        if(!message.getUserId().equals(userId)){
            throw new BusinessException("Only the creator can edit his/her message.");
        }

        message.setContent(content);
        messageRepository.save(message);

        return MessageMapper.toUpdateMessageResponseDto(message);
    }

    @Override
    public MarkMessageAsReadResponseDto markMessagesAsRead(UUID userId, UUID teamId, List<UUID> messageIds) {
        List<UUID> validatedList = validateMarkAsReadRequest(userId, teamId, messageIds);
        statusService.markMessagesAsRead(userId, validatedList);
        return MessageMapper.toMarkMessageAsReadResponseDto(userId, validatedList);
    }

    @Override
    public DeleteMessageResponseDto deleteMessage(UUID userId, UUID messageId) {
        Message message = messageRepository.findById(messageId).orElseThrow(
                () -> new BusinessException("Message not found.")
        );

        if(!message.getUserId().equals(userId)){
            throw new BusinessException("Only the creator can delete his/her message.");
        }

        message.setIsDeleted(true);
        messageRepository.save(message);

        return MessageMapper.toDeleteMessageResponseDto(message);
    }

    @Override
    public UUID getTeamId(UUID messageId) {
        Message message = messageRepository.findById(messageId).orElseThrow(
                () -> new NotFoundException("Message not found.")
        );

        return message.getTeamId();
    }

    @Override
    @Transactional
    public void deleteAllMessagesInTeam(UUID teamId) {
        List<UUID> teamMessagesIds = messageRepository.findByTeamId(teamId).stream()
                .map(Message::getId)
                .toList();

        for(UUID teamMessageId : teamMessagesIds){
            statusService.deleteAllReadStatus(teamMessageId);
        }

        messageRepository.deleteAllByTeamId(teamId);
    }

    private List<MessageResponse> toListMessageResponse(List<Message> messages) {
        List<MessageResponse> result = new ArrayList<>();

        for(Message message : messages) {
            UserDetailResponse user = userClient.getUserById(message.getUserId());

            List<UUID> readByIds = statusService.getReadByList(message.getId());
            List<String> readBy = new ArrayList<>();

            for(UUID readById : readByIds){
                UserDetailResponse readUser = userClient.getUserById(readById);
                readBy.add(readUser.getUsername());
            }

            MessageResponse response = MessageMapper.toMessageResponse(message, user, readBy);
            result.add(response);
        }

        return result;
    }

    private List<UUID> validateMarkAsReadRequest(UUID userId, UUID teamId, List<UUID> messageIds) {
        if(messageIds.isEmpty()){
            throw new BusinessException("No messages found.");
        }

        teamClient.validateUsersInTeam(teamId, Set.of(userId));

        List<UUID> result = new ArrayList<>();

        for(UUID messageId : messageIds){
            Message message = messageRepository.findById(messageId).orElseThrow(
                    () -> new BusinessException("Message with id " + messageId + " not found.")
            );

            if(!message.getTeamId().equals(teamId)){
                throw new BusinessException("The message with id " + messageId + " is not from the team " + teamId + ".");
            }

            if(!message.getUserId().equals(userId)){
                result.add(messageId);
            }
        }

        return result;
    }
}
