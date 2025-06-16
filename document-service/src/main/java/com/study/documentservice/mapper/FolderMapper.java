package com.study.documentservice.mapper;

import com.study.documentservice.entity.Folder;
import com.study.documentservice.grpc.FolderDetailResponse;
import com.study.documentservice.grpc.FolderResponse;
import com.study.documentservice.grpc.FoldersResponse;

import java.util.List;

public class FolderMapper {
    private FolderMapper() {}

    public static FolderResponse toFolderResponse(Folder folder) {
        return FolderResponse.newBuilder()
                .setId(folder.getId().toString())
                .setName(folder.getName())
                .setDocumentCount(folder.getDocumentCount())
                .build();
    }

    public static FolderDetailResponse toFolderDetailResponse(Folder folder) {
        return FolderDetailResponse.newBuilder()
                .setId(folder.getId().toString())
                .setName(folder.getName())
                .setCreatedBy(folder.getCreatedBy().toString())
                .setCreatedAt(folder.getCreatedAt().toString())
                .setUpdatedBy(folder.getUpdatedBy().toString())
                .setUpdatedAt(folder.getUpdatedAt().toString())
                .setBytes(folder.getBytes())
                .setDocumentCount(folder.getDocumentCount())
                .build();
    }

    public static FoldersResponse toFoldersResponse(List<Folder> folders, long total, String nextCursor) {
        List<String> names = folders.stream().map(Folder::getName).toList();

        return FoldersResponse.newBuilder()
                .addAllNames(names)
                .setTotal(total)
                .setNextCursor(nextCursor)
                .build();
    }
}
