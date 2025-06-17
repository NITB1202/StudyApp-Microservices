package com.study.chatservice.service.impl;

import com.study.chatservice.dto.request.SendMessageRequestDto;
import com.study.chatservice.dto.request.UpdateMessageRequestDto;
import com.study.chatservice.dto.response.MessageResponseDto;
import com.study.chatservice.dto.response.UpdateMessageResponseDto;
import com.study.chatservice.entity.Message;
import com.study.chatservice.grpc.DocumentServiceGrpcClient;
import com.study.chatservice.grpc.GetMessagesRequest;
import com.study.chatservice.grpc.TeamServiceGrpcClient;
import com.study.chatservice.grpc.UserServiceGrpcClient;
import com.study.chatservice.mapper.MessageMapper;
import com.study.chatservice.repository.MessageRepository;
import com.study.chatservice.service.MessageService;
import com.study.common.exceptions.BusinessException;
import com.study.common.utils.FileUtils;
import com.study.userservice.grpc.UserDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;

    private final UserServiceGrpcClient userClient;
    private final TeamServiceGrpcClient teamClient;
    private final DocumentServiceGrpcClient documentClient;

    private static final int DEFAULT_SIZE = 10;
    private final static String FOLDER_PATH = "chats";

    @Override
    public List<Message> getMessages(GetMessagesRequest request) {
        UUID teamId = UUID.fromString(request.getTeamId());
        int size = request.getSize() > 0 ? request.getSize() : DEFAULT_SIZE;

        Pageable pageable = PageRequest.of(0, size, Sort.by("createdAt").descending());

        if(request.getCursor().isEmpty()){
            return messageRepository.findByTeamId(teamId, pageable);
        }

        LocalDateTime cursor = LocalDateTime.parse(request.getCursor());
        return messageRepository.findByTeamIdAndCreatedAtLessThan(teamId, cursor, pageable);
    }

    @Override
    public long countTeamMessages(UUID teamId) {
        return messageRepository.countByTeamId(teamId);
    }

    @Override
    public MessageResponseDto saveMessage(UUID userId, UUID teamId, SendMessageRequestDto dto) {
        teamClient.validateUsersInTeam(teamId, Set.of(userId));

        Message message = Message.builder()
                .userId(userId)
                .teamId(teamId)
                .content(dto.getContent())
                .createdAt(LocalDateTime.now())
                .isDeleted(false)
                .build();

        messageRepository.save(message);
        UserDetailResponse user = userClient.getUserById(userId);

        return MessageMapper.toMessageResponseDto(message, user);
    }

    @Override
    public MessageResponseDto saveImageMessage(UUID userId, UUID teamId, MultipartFile file) {
        teamClient.validateUsersInTeam(teamId, Set.of(userId));

        if(!FileUtils.isImage(file)) {
            throw new BusinessException("This is not an image.");
        }

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

        try {
            String url = documentClient.uploadImage(folderPath, publicId, file.getBytes()).getUrl();
            message.setImageUrl(url);
            messageRepository.save(message);
        } catch (IOException e) {
            throw new BusinessException("Upload image failed.");
        }

        UserDetailResponse user = userClient.getUserById(userId);

        return MessageMapper.toMessageResponseDto(message, user);
    }

    @Override
    public UpdateMessageResponseDto updateMessage(UUID userId, UUID messageId, UpdateMessageRequestDto dto) {
        Message message = messageRepository.findById(messageId).orElseThrow(
                () -> new BusinessException("Message not found.")
        );

        if(!message.getUserId().equals(userId)){
            throw new BusinessException("Only the creator can edit his/her message.");
        }

        message.setContent(dto.getContent());
        messageRepository.save(message);

        return MessageMapper.toUpdateMessageResponseDto(message);
    }

    @Override
    public void deleteMessage(UUID userId, UUID messageId) {
        Message message = messageRepository.findById(messageId).orElseThrow(
                () -> new BusinessException("Message not found.")
        );

        if(!message.getUserId().equals(userId)){
            throw new BusinessException("Only the creator can delete his/her message.");
        }

        message.setIsDeleted(true);
        messageRepository.save(message);
    }

    @Override
    public void deleteAllMessagesInTeam(UUID teamId) {
        messageRepository.deleteAllByTeamId(teamId);
    }
}
