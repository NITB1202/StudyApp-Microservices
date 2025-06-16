package com.study.apigateway.service.Document;

import com.study.apigateway.dto.Action.ActionResponseDto;
import com.study.apigateway.dto.Document.Folder.request.CreateFolderRequestDto;
import com.study.apigateway.dto.Document.Folder.response.FolderDetailResponseDto;
import com.study.apigateway.dto.Document.Folder.response.FolderResponseDto;
import com.study.apigateway.dto.Document.Folder.response.FoldersResponseDto;

import java.time.LocalDateTime;
import java.util.UUID;

public interface FolderService {
    FolderResponseDto createPersonalFolder(UUID userId, CreateFolderRequestDto request);
    FolderResponseDto createTeamFolder(UUID userId, UUID teamId, CreateFolderRequestDto request);
    FolderDetailResponseDto getFolderById(UUID id);
    FoldersResponseDto getPersonalFolders(UUID userId, LocalDateTime cursor, int size);
    FoldersResponseDto getTeamFolders(UUID teamId, LocalDateTime cursor, int size);
    FoldersResponseDto searchPersonalFolderByName(UUID userId, String keyword, LocalDateTime cursor, int size);
    FoldersResponseDto searchTeamFolderByName(UUID teamId, String keyword, LocalDateTime cursor, int size);
    ActionResponseDto updateFolderName(UUID userId, UUID id, String name);
    ActionResponseDto deleteFolder(UUID userId, UUID id);
}
