package com.study.apigateway.service.Document.impl;

import com.study.apigateway.dto.Action.ActionResponseDto;
import com.study.apigateway.dto.Document.Folder.request.CreateFolderRequestDto;
import com.study.apigateway.dto.Document.Folder.response.FolderDetailResponseDto;
import com.study.apigateway.dto.Document.Folder.response.FolderResponseDto;
import com.study.apigateway.dto.Document.Folder.response.FoldersResponseDto;
import com.study.apigateway.grpc.DocumentServiceGrpcClient;
import com.study.apigateway.grpc.TeamServiceGrpcClient;
import com.study.apigateway.grpc.UserServiceGrpcClient;
import com.study.apigateway.mapper.ActionMapper;
import com.study.apigateway.mapper.FolderMapper;
import com.study.apigateway.service.Document.FolderService;
import com.study.common.exceptions.BusinessException;
import com.study.common.grpc.ActionResponse;
import com.study.documentservice.grpc.FolderDetailResponse;
import com.study.documentservice.grpc.FolderResponse;
import com.study.documentservice.grpc.FoldersResponse;
import com.study.documentservice.grpc.TeamFolderResponse;
import com.study.userservice.grpc.UserDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FolderServiceImpl implements FolderService {
    private final DocumentServiceGrpcClient documentClient;
    private final UserServiceGrpcClient userClient;
    private final TeamServiceGrpcClient teamClient;

    @Override
    public Mono<FolderResponseDto> createPersonalFolder(UUID userId, CreateFolderRequestDto request) {
        return Mono.fromCallable(()->{
            FolderResponse response = documentClient.createFolder(userId, null, request);
            return FolderMapper.toFolderResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<FolderResponseDto> createTeamFolder(UUID userId, UUID teamId, CreateFolderRequestDto request) {
        return Mono.fromCallable(()->{
            teamClient.validateUpdateTeamResource(userId, teamId);
            FolderResponse response = documentClient.createFolder(userId, teamId, request);
            return FolderMapper.toFolderResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<FolderDetailResponseDto> getFolderById(UUID id) {
        return Mono.fromCallable(()->{
            FolderDetailResponse response = documentClient.getFolderById(id);

            UUID createdById = UUID.fromString(response.getCreatedBy());
            UserDetailResponse createdBy = userClient.getUserById(createdById);

            UUID updatedById = UUID.fromString(response.getUpdatedBy());
            UserDetailResponse updatedBy = userClient.getUserById(updatedById);

            return FolderMapper.toFolderDetailResponseDto(response, createdBy, updatedBy);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<FoldersResponseDto> getPersonalFolders(UUID userId, LocalDateTime cursor, int size) {
        return Mono.fromCallable(()->{
            FoldersResponse response = documentClient.getFolders(userId, null, cursor, size);
            return FolderMapper.toFoldersResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<FoldersResponseDto> getTeamFolders(UUID teamId, LocalDateTime cursor, int size) {
        return Mono.fromCallable(()->{
            FoldersResponse response = documentClient.getFolders(null, teamId, cursor, size);
            return FolderMapper.toFoldersResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<FoldersResponseDto> searchPersonalFolderByName(UUID userId, String keyword, LocalDateTime cursor, int size) {
        return Mono.fromCallable(()->{
            FoldersResponse response = documentClient.searchFolderByName(userId, null, keyword, cursor, size);
            return FolderMapper.toFoldersResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<FoldersResponseDto> searchTeamFolderByName(UUID teamId, String keyword, LocalDateTime cursor, int size) {
        return Mono.fromCallable(()->{
            FoldersResponse response = documentClient.searchFolderByName(null, teamId, keyword, cursor, size);
            return FolderMapper.toFoldersResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<ActionResponseDto> updateFolderName(UUID userId, UUID id, String name) {
        return Mono.fromCallable(()->{
            validateUpdateFolder(userId, id);
            ActionResponse response = documentClient.updateFolderName(userId, id, name);
            return ActionMapper.toResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<ActionResponseDto> deleteFolder(UUID userId, UUID id) {
        return Mono.fromCallable(()->{
            validateUpdateFolder(userId, id);
            ActionResponse response = documentClient.deleteDocument(id, userId);
            return ActionMapper.toResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    private void validateUpdateFolder(UUID userId, UUID folderId) {
        if(!documentClient.isFolderCreator(userId, folderId)) {
            TeamFolderResponse check = documentClient.isTeamFolder(folderId);

            if(!check.getIsTeamFolder()) {
                throw new BusinessException("You can't update other's folder.");
            }
            else {
                UUID teamId = UUID.fromString(check.getTeamId());
                teamClient.validateUpdateTeamResource(userId, teamId);
            }
        }
    }
}
