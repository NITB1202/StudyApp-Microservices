package com.study.documentservice.controller;

import com.study.common.grpc.ActionResponse;
import com.study.documentservice.entity.Folder;
import com.study.documentservice.entity.TeamUsage;
import com.study.documentservice.entity.UserUsage;
import com.study.documentservice.grpc.*;
import com.study.documentservice.mapper.FolderMapper;
import com.study.documentservice.mapper.UsageMapper;
import com.study.documentservice.service.FolderService;
import com.study.documentservice.service.UsageService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;

@GrpcService
@RequiredArgsConstructor
public class DocumentController extends DocumentServiceGrpc.DocumentServiceImplBase {
    private final UsageService usageService;
    private final FolderService folderService;

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
    public void uploadDocument(UploadDocumentRequest request, StreamObserver<DocumentResponse> responseObserver) {

    }

    @Override
    public void getDocumentById(GetDocumentByIdRequest request, StreamObserver<DocumentDetailResponse> responseObserver) {

    }

    @Override
    public void getDocuments(GetDocumentsRequest request, StreamObserver<DocumentsResponse> responseObserver) {

    }

    @Override
    public void searchDocumentByName(SearchDocumentByNameRequest request, StreamObserver<DocumentsResponse> responseObserver) {

    }

    @Override
    public void updateDocumentName(UpdateDocumentNameRequest request, StreamObserver<ActionResponse> responseObserver) {

    }

    @Override
    public void moveDocument(MoveDocumentRequest request, StreamObserver<ActionResponse> responseObserver) {

    }

    @Override
    public void deleteDocument(DeleteDocumentRequest request, StreamObserver<ActionResponse> responseObserver) {

    }

    //Image
    @Override
    public void uploadImage(UploadImageRequest request, StreamObserver<UploadImageResponse> responseObserver) {

    }
}
