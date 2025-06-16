package com.study.apigateway.grpc;

import com.google.protobuf.ByteString;
import com.study.common.grpc.ActionResponse;
import com.study.documentservice.grpc.*;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class DocumentServiceGrpcClient {
    @GrpcClient("document-service")
    private DocumentServiceGrpc.DocumentServiceBlockingStub blockingStub;

    //Usage
    public UsageResponse getUserUsage(UUID userId) {
        GetUserUsageRequest request = GetUserUsageRequest.newBuilder()
                .setUserId(userId.toString())
                .build();

        return blockingStub.getUserUsage(request);
    }

    public UsageResponse getTeamUsage(UUID teamId) {
        GetTeamUsageRequest request = GetTeamUsageRequest.newBuilder()
                .setTeamId(teamId.toString())
                .build();

        return blockingStub.getTeamUsage(request);
    }

    //Folder
    public boolean isFolderCreator(UUID userId, UUID folderId) {
        IsFolderCreatorRequest request = IsFolderCreatorRequest.newBuilder()
                .setUserId(userId.toString())
                .setFolderId(folderId.toString())
                .build();

        return blockingStub.isFolderCreator(request).getIsFolderCreator();
    }

    public IsTeamFolderResponse isTeamFolder(UUID folderId) {
        IsTeamFolderRequest request = IsTeamFolderRequest.newBuilder()
                .setId(folderId.toString())
                .build();

        return blockingStub.isTeamFolder(request);
    }

    public FolderResponse createFolder(UUID userId, UUID teamId, String name) {
        String teamIdStr = teamId != null ? teamId.toString() : "";

        CreateFolderRequest request = CreateFolderRequest.newBuilder()
                .setUserId(userId.toString())
                .setTeamId(teamIdStr)
                .setName(name)
                .build();

        return blockingStub.createFolder(request);
    }

    public FolderDetailResponse getFolderById(UUID id) {
        GetFolderByIdRequest request = GetFolderByIdRequest.newBuilder()
                .setId(id.toString())
                .build();

        return blockingStub.getFolderById(request);
    }

    public FoldersResponse getFolders(UUID userId, UUID teamId, LocalDateTime cursor, int size) {
        String userIdStr = userId != null ? userId.toString() : "";
        String teamIdStr = teamId != null ? teamId.toString() : "";
        String cursorStr = cursor != null ? cursor.toString() : "";

        GetFoldersRequest request = GetFoldersRequest.newBuilder()
                .setUserId(userIdStr)
                .setTeamId(teamIdStr)
                .setCursor(cursorStr)
                .setSize(size)
                .build();

        return blockingStub.getFolders(request);
    }

    public FoldersResponse searchFolderByName(UUID userId, UUID teamId, String keyword, LocalDateTime cursor, int size) {
        String userIdStr = userId != null ? userId.toString() : "";
        String teamIdStr = teamId != null ? teamId.toString() : "";
        String cursorStr = cursor != null ? cursor.toString() : "";

        SearchFolderByNameRequest request = SearchFolderByNameRequest.newBuilder()
                .setUserId(userIdStr)
                .setTeamId(teamIdStr)
                .setKeyword(keyword)
                .setCursor(cursorStr)
                .setSize(size)
                .build();

        return blockingStub.searchFolderByName(request);
    }

    public ActionResponse updateFolderName(UUID userId, UUID id, String name) {
        UpdateFolderNameRequest request = UpdateFolderNameRequest.newBuilder()
                .setId(id.toString())
                .setUserId(userId.toString())
                .setName(name)
                .build();

        return blockingStub.updateFolderName(request);
    }

    public ActionResponse deleteFolder(UUID id) {
        DeleteFolderRequest request = DeleteFolderRequest.newBuilder()
                .setId(id.toString())
                .build();

        return blockingStub.deleteFolder(request);
    }

    //Document
    public boolean isDocumentCreator(UUID userId, UUID documentId) {
        IsDocumentCreatorRequest request = IsDocumentCreatorRequest.newBuilder()
                .setUserId(userId.toString())
                .setDocumentId(documentId.toString())
                .build();

        return blockingStub.isDocumentCreator(request).getIsDocumentCreator();
    }

    public DocumentResponse uploadDocument(UUID userId, UUID folderId, String name, byte[] file) {
        ByteString byteString = ByteString.copyFrom(file);

        UploadDocumentRequest request = UploadDocumentRequest.newBuilder()
                .setUserId(userId.toString())
                .setFolderId(folderId.toString())
                .setName(name)
                .setFile(byteString)
                .build();

        return blockingStub.uploadDocument(request);
    }

    public DocumentDetailResponse getDocumentById(UUID id) {
        GetDocumentByIdRequest request = GetDocumentByIdRequest.newBuilder()
                .setId(id.toString())
                .build();

        return blockingStub.getDocumentById(request);
    }

    public DocumentsResponse getDocuments(UUID folderId, LocalDateTime cursor, int size) {
        String cursorStr = cursor != null ? cursor.toString() : "";

        GetDocumentsRequest request = GetDocumentsRequest.newBuilder()
                .setFolderId(folderId.toString())
                .setCursor(cursorStr)
                .setSize(size)
                .build();

        return blockingStub.getDocuments(request);
    }

    public DocumentsResponse searchDocumentByName(UUID folderId, String keyword, LocalDateTime cursor, int size) {
        String cursorStr = cursor != null ? cursor.toString() : "";

        SearchDocumentByNameRequest request = SearchDocumentByNameRequest.newBuilder()
                .setFolderId(folderId.toString())
                .setKeyword(keyword)
                .setCursor(cursorStr)
                .setSize(size)
                .build();

        return blockingStub.searchDocumentByName(request);
    }

    public ActionResponse updateDocumentName(UUID id, UUID userId, String name) {
        UpdateDocumentNameRequest request = UpdateDocumentNameRequest.newBuilder()
                .setId(id.toString())
                .setUserId(userId.toString())
                .setName(name)
                .build();

        return blockingStub.updateDocumentName(request);
    }

    public ActionResponse moveDocument(UUID userId, UUID id, UUID newFolderId) {
        MoveDocumentRequest request = MoveDocumentRequest.newBuilder()
                .setId(id.toString())
                .setUserId(userId.toString())
                .setNewFolderId(newFolderId.toString())
                .build();

        return blockingStub.moveDocument(request);
    }

    public ActionResponse deleteDocument(UUID id, UUID userId) {
        DeleteDocumentRequest request = DeleteDocumentRequest.newBuilder()
                .setId(id.toString())
                .setUserId(userId.toString())
                .build();

        return blockingStub.deleteDocument(request);
    }

    //Image
    public UploadImageResponse uploadImage(String folderPath, String publicId, byte[] file) {
        ByteString byteString = ByteString.copyFrom(file);

        UploadImageRequest request = UploadImageRequest.newBuilder()
                .setFolderPath(folderPath)
                .setPublicId(publicId)
                .setFile(byteString)
                .build();

        return blockingStub.uploadImage(request);
    }
}
