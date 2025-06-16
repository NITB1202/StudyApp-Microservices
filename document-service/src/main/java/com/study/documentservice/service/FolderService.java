package com.study.documentservice.service;

import com.study.documentservice.entity.Folder;
import com.study.documentservice.grpc.*;

import java.util.List;
import java.util.UUID;

public interface FolderService {
    Folder createFolder(CreateFolderRequest request);
    Folder getFolderById(GetFolderByIdRequest request);
    List<Folder> getFolders(GetFoldersRequest request);
    long getFolderCount(GetFoldersRequest request);
    List<Folder> searchFolderByName(SearchFolderByNameRequest request);
    long getFolderCountWithKeyword(SearchFolderByNameRequest request);
    void updateFolderName(UpdateFolderNameRequest request);
    void deleteFolder(DeleteFolderRequest request);
    boolean existsById(UUID id);
    boolean validateMoveDocument(UUID oldFolderId, UUID newFolderId);
    void addDocument(UUID folderId, UUID userId, long bytes);
    void removeDocument(UUID folderId, UUID userId, long bytes);
    List<UUID> getAllIdsByTeamId(UUID teamId);
    void deleteAllById(List<UUID> ids);
}
