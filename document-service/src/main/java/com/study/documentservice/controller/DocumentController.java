package com.study.documentservice.controller;

import com.study.common.grpc.ActionResponse;
import com.study.documentservice.entity.TeamUsage;
import com.study.documentservice.entity.UserUsage;
import com.study.documentservice.grpc.*;
import com.study.documentservice.mapper.UsageMapper;
import com.study.documentservice.service.FolderService;
import com.study.documentservice.service.UsageService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

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

    }

    @Override
    public void getFolderById(GetFolderByIdRequest request, StreamObserver<FolderDetailResponse> responseObserver) {

    }

    @Override
    public void getFolders(GetFoldersRequest request, StreamObserver<FoldersResponse> responseObserver) {

    }

    @Override
    public void searchFolderByName(SearchFolderByNameRequest request, StreamObserver<FoldersResponse> responseObserver) {

    }

    @Override
    public void updateFolderName(UpdateFolderNameRequest request, StreamObserver<ActionResponse> responseObserver) {

    }

    @Override
    public void deleteFolder(DeleteFolderRequest request, StreamObserver<ActionResponse> responseObserver) {

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
