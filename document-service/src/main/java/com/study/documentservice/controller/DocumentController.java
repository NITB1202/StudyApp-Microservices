package com.study.documentservice.controller;

import com.study.common.grpc.ActionResponse;
import com.study.documentservice.entity.Document;
import com.study.documentservice.entity.Folder;
import com.study.documentservice.entity.TeamUsage;
import com.study.documentservice.entity.UserUsage;
import com.study.documentservice.grpc.*;
import com.study.documentservice.mapper.DocumentMapper;
import com.study.documentservice.mapper.FolderMapper;
import com.study.documentservice.mapper.UsageMapper;
import com.study.documentservice.service.DocumentService;
import com.study.documentservice.service.FolderService;
import com.study.documentservice.service.UsageService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;
import java.util.UUID;

@GrpcService
@RequiredArgsConstructor
public class DocumentController extends DocumentServiceGrpc.DocumentServiceImplBase {
    private final UsageService usageService;
    private final FolderService folderService;
    private final DocumentService documentService;

    //Usage
    @Override
    public void getUserUsage(GetUserUsageRequest request, StreamObserver<UsageResponse> responseObserver) {
        UserUsage usage = usageService.getUserUsage(request);
        UsageResponse response = UsageMapper.toUsageResponse(usage);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getTeamUsage(GetTeamUsageRequest request, StreamObserver<UsageResponse> responseObserver) {
        TeamUsage usage = usageService.getTeamUsage(request);
        UsageResponse response = UsageMapper.toUsageResponse(usage);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    //Folder
    @Override
    public void isFolderCreator(IsFolderCreatorRequest request, StreamObserver<IsFolderCreatorResponse> responseObserver) {
        boolean isFolderCreator = folderService.isFolderCreator(request);

        IsFolderCreatorResponse response = IsFolderCreatorResponse.newBuilder()
                .setIsFolderCreator(isFolderCreator)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void isTeamFolder(IsTeamFolderRequest request, StreamObserver<TeamFolderResponse> responseObserver) {
        UUID teamId = folderService.isTeamFolder(request);
        String teamIdStr = teamId != null ? teamId.toString() : "";

        TeamFolderResponse response = TeamFolderResponse.newBuilder()
                .setIsTeamFolder(teamId != null)
                .setTeamId(teamIdStr)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void createFolder(CreateFolderRequest request, StreamObserver<FolderResponse> responseObserver) {
        Folder folder = folderService.createFolder(request);
        FolderResponse response = FolderMapper.toFolderResponse(folder);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getFolderById(GetFolderByIdRequest request, StreamObserver<FolderDetailResponse> responseObserver) {
        Folder folder = folderService.getFolderById(request);
        FolderDetailResponse response = FolderMapper.toFolderDetailResponse(folder);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getFolders(GetFoldersRequest request, StreamObserver<FoldersResponse> responseObserver) {
        List<Folder> folders = folderService.getFolders(request);
        long total = folderService.getFolderCount(request);
        String nextCursor = !folders.isEmpty() && folders.size() == request.getSize() ?
                folders.get(folders.size() - 1).getCreatedAt().toString() : "";

        FoldersResponse response = FolderMapper.toFoldersResponse(folders, total, nextCursor);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void searchFolderByName(SearchFolderByNameRequest request, StreamObserver<FoldersResponse> responseObserver) {
        List<Folder> folders = folderService.searchFolderByName(request);
        long total = folderService.getFolderCountWithKeyword(request);
        String nextCursor = !folders.isEmpty() && folders.size() == request.getSize() ?
                folders.get(folders.size() - 1).getCreatedAt().toString() : "";

        FoldersResponse response = FolderMapper.toFoldersResponse(folders, total, nextCursor);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void updateFolderName(UpdateFolderNameRequest request, StreamObserver<ActionResponse> responseObserver) {
        folderService.updateFolderName(request);

        ActionResponse response = ActionResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Update successfully.")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void deleteFolder(DeleteFolderRequest request, StreamObserver<ActionResponse> responseObserver) {
        UUID id = UUID.fromString(request.getId());
        documentService.deleteAllDocuments(id);
        folderService.deleteFolder(request);

        ActionResponse response = ActionResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Delete successfully.")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    //Document
    @Override
    public void isDocumentCreator(IsDocumentCreatorRequest request, StreamObserver<IsDocumentCreatorResponse> responseObserver) {
        boolean isDocumentCreator = documentService.isDocumentCreator(request);

        IsDocumentCreatorResponse response = IsDocumentCreatorResponse.newBuilder()
                .setIsDocumentCreator(isDocumentCreator)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void inTeamFolder(InTeamFolderRequest request, StreamObserver<TeamFolderResponse> responseObserver) {
        UUID teamId = documentService.inTeamFolder(request);
        String teamIdStr = teamId != null ? teamId.toString() : "";

        TeamFolderResponse response = TeamFolderResponse.newBuilder()
                .setIsTeamFolder(teamId != null)
                .setTeamId(teamIdStr)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void uploadDocument(UploadDocumentRequest request, StreamObserver<DocumentResponse> responseObserver) {
        Document document = documentService.uploadDocument(request);
        DocumentResponse response = DocumentMapper.toDocumentResponse(document);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getDocumentById(GetDocumentByIdRequest request, StreamObserver<DocumentDetailResponse> responseObserver) {
        Document document = documentService.getDocumentById(request);
        DocumentDetailResponse response = DocumentMapper.toDocumentDetailResponse(document);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getDocuments(GetDocumentsRequest request, StreamObserver<DocumentsResponse> responseObserver) {
        UUID folderId = UUID.fromString(request.getFolderId());

        List<Document> documents = documentService.getDocuments(request);
        long total = documentService.getDocumentCount(folderId);
        String nextCursor = !documents.isEmpty() && documents.size() == request.getSize() ?
                documents.get(documents.size() - 1).getCreatedAt().toString() : "";

        DocumentsResponse response = DocumentMapper.toDocumentsResponse(documents, total, nextCursor);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void searchDocumentByName(SearchDocumentByNameRequest request, StreamObserver<DocumentsResponse> responseObserver) {
        UUID folderId = UUID.fromString(request.getFolderId());

        List<Document> documents = documentService.searchDocumentByName(request);
        long total = documentService.getDocumentCountWithKeyword(folderId, request.getKeyword());
        String nextCursor = !documents.isEmpty() && documents.size() == request.getSize() ?
                documents.get(documents.size() - 1).getCreatedAt().toString() : "";

        DocumentsResponse response = DocumentMapper.toDocumentsResponse(documents, total, nextCursor);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void updateDocumentName(UpdateDocumentNameRequest request, StreamObserver<ActionResponse> responseObserver) {
        documentService.updateDocumentName(request);

        ActionResponse response = ActionResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Update successfully.")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void moveDocument(MoveDocumentRequest request, StreamObserver<ActionResponse> responseObserver) {
        documentService.moveDocument(request);

        ActionResponse response = ActionResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Move successfully.")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void deleteDocument(DeleteDocumentRequest request, StreamObserver<ActionResponse> responseObserver) {
        documentService.deleteDocument(request);

        ActionResponse response = ActionResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Delete successfully.")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
