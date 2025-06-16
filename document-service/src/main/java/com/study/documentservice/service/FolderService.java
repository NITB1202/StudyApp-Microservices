package com.study.documentservice.service;

import com.study.documentservice.entity.Folder;
import com.study.documentservice.grpc.*;

import java.util.List;
import java.util.UUID;

public interface FolderService {
    Folder createFolder(CreateFolderRequest request);
    Folder getFolderById(GetFolderByIdRequest request);
    List<Folder> getFolders(GetFoldersRequest request);
    List<Folder> searchFolderByName(SearchFolderByNameRequest request);
    int getUserFolderCount(UUID userId);
    int getTeamFolderCount(UUID teamId);
    int getUserFolderCountWithKeyword(UUID userId, String keyword);
    int getTeamFolderCountWithKeyword(UUID teamId, String keyword);
    void updateFolderName(UpdateFolderNameRequest request);
    void deleteFolder(DeleteFolderRequest request);
    void addDocument(UUID folderId, UUID userId, long bytes);
    void removeDocument(UUID folderId, UUID userId, long bytes);
}
