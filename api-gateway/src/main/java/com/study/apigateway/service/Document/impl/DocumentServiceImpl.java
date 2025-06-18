package com.study.apigateway.service.Document.impl;

import com.study.apigateway.dto.Action.ActionResponseDto;
import com.study.apigateway.dto.Document.Document.DocumentDetailResponseDto;
import com.study.apigateway.dto.Document.Document.DocumentResponseDto;
import com.study.apigateway.dto.Document.Document.DocumentsResponseDto;
import com.study.apigateway.grpc.DocumentServiceGrpcClient;
import com.study.apigateway.grpc.TeamServiceGrpcClient;
import com.study.apigateway.grpc.UserServiceGrpcClient;
import com.study.apigateway.mapper.ActionMapper;
import com.study.apigateway.mapper.DocumentMapper;
import com.study.apigateway.service.Document.DocumentService;
import com.study.common.utils.FileUtils;
import com.study.common.exceptions.BusinessException;
import com.study.common.grpc.ActionResponse;
import com.study.documentservice.grpc.DocumentDetailResponse;
import com.study.documentservice.grpc.DocumentResponse;
import com.study.documentservice.grpc.DocumentsResponse;
import com.study.documentservice.grpc.TeamFolderResponse;
import com.study.userservice.grpc.UserDetailResponse;
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
public class DocumentServiceImpl implements DocumentService {
    private final DocumentServiceGrpcClient documentClient;
    private final UserServiceGrpcClient userClient;
    private final TeamServiceGrpcClient teamClient;

    @Override
    public Mono<DocumentResponseDto> uploadDocument(UUID userId, UUID folderId, String name, FilePart file) {
        if(!FileUtils.isDocument(file)) {
            throw new BusinessException("Invalid file type.");
        }

        return DataBufferUtils.join(file.content())
                .flatMap(buffer -> {
                    byte[] bytes = new byte[buffer.readableByteCount()];
                    buffer.read(bytes);
                    DataBufferUtils.release(buffer);

                    DocumentResponse response = documentClient.uploadDocument(userId, folderId, name, bytes);

                    return Mono.fromCallable(() -> DocumentMapper.toDocumentResponseDto(response))
                            .subscribeOn(Schedulers.boundedElastic());
                });
    }

    @Override
    public Mono<DocumentDetailResponseDto> getDocumentById(UUID id) {
        return Mono.fromCallable(()->{
            DocumentDetailResponse response = documentClient.getDocumentById(id);

            UUID createdById = UUID.fromString(response.getCreatedBy());
            UserDetailResponse createdBy = userClient.getUserById(createdById);

            UUID updatedById = UUID.fromString(response.getUpdatedBy());
            UserDetailResponse updatedBy = userClient.getUserById(updatedById);

            return DocumentMapper.toDocumentDetailResponseDto(response, createdBy, updatedBy);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<DocumentsResponseDto> getDocuments(UUID folderId, LocalDateTime cursor, int size) {
        return Mono.fromCallable(()->{
            DocumentsResponse response = documentClient.getDocuments(folderId, cursor, size);
            return DocumentMapper.toDocumentsResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<DocumentsResponseDto> searchDocumentByName(UUID folderId, String keyword, LocalDateTime cursor, int size) {
        return Mono.fromCallable(()->{
            DocumentsResponse response = documentClient.searchDocumentByName(folderId, keyword, cursor, size);
            return DocumentMapper.toDocumentsResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<ActionResponseDto> updateDocumentName(UUID userId, UUID id, String name) {
        return Mono.fromCallable(()->{
            validateUpdateDocument(userId, id);
            ActionResponse response = documentClient.updateDocumentName(id, userId, name);
            return ActionMapper.toResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<ActionResponseDto> moveDocument(UUID userId, UUID id, UUID newFolderId) {
        return Mono.fromCallable(()->{
            validateUpdateDocument(userId, id);
            ActionResponse response = documentClient.moveDocument(userId, id, newFolderId);
            return ActionMapper.toResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<ActionResponseDto> deleteDocument(UUID userId, UUID id) {
        return Mono.fromCallable(()->{
            validateUpdateDocument(userId, id);
            ActionResponse response = documentClient.deleteDocument(id, userId);
            return ActionMapper.toResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    private void validateUpdateDocument(UUID userId, UUID documentId) {
        if(!documentClient.isDocumentCreator(userId, documentId)) {
            TeamFolderResponse check = documentClient.inTeamFolder(documentId);

            if(!check.getIsTeamFolder()) {
                throw new BusinessException("You can't update document in other's folder.");
            }
            else {
                UUID teamId = UUID.fromString(check.getTeamId());
                teamClient.validateUpdateTeamResource(userId, teamId);
            }
        }
    }
}
