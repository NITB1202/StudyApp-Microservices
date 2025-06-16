package com.study.apigateway.service.Document;

import com.study.apigateway.dto.Action.ActionResponseDto;
import com.study.apigateway.dto.Document.Folder.request.CreateFolderRequestDto;
import com.study.apigateway.dto.Document.Folder.response.FolderDetailResponseDto;
import com.study.apigateway.dto.Document.Folder.response.FolderResponseDto;
import com.study.apigateway.dto.Document.Folder.response.FoldersResponseDto;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

public interface FolderService {
    Mono<FolderResponseDto> createPersonalFolder(UUID userId, CreateFolderRequestDto request);
    Mono<FolderResponseDto> createTeamFolder(UUID userId, UUID teamId, CreateFolderRequestDto request);
    Mono<FolderDetailResponseDto> getFolderById(UUID id);
    Mono<FoldersResponseDto> getPersonalFolders(UUID userId, LocalDateTime cursor, int size);
    Mono<FoldersResponseDto> getTeamFolders(UUID teamId, LocalDateTime cursor, int size);
    Mono<FoldersResponseDto> searchPersonalFolderByName(UUID userId, String keyword, LocalDateTime cursor, int size);
    Mono<FoldersResponseDto> searchTeamFolderByName(UUID teamId, String keyword, LocalDateTime cursor, int size);
    Mono<ActionResponseDto> updateFolderName(UUID userId, UUID id, String name);
    Mono<ActionResponseDto> deleteFolder(UUID userId, UUID id);
}
