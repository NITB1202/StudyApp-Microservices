package com.study.apigateway.mapper;

import com.study.apigateway.dto.Document.Folder.response.FolderDetailResponseDto;
import com.study.apigateway.dto.Document.Folder.response.FolderResponseDto;
import com.study.apigateway.dto.Document.Folder.response.FolderSummaryResponseDto;
import com.study.apigateway.dto.Document.Folder.response.FoldersResponseDto;
import com.study.documentservice.grpc.FolderDetailResponse;
import com.study.documentservice.grpc.FolderResponse;
import com.study.documentservice.grpc.FolderSummaryResponse;
import com.study.documentservice.grpc.FoldersResponse;
import com.study.userservice.grpc.UserDetailResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class FolderMapper {
    private FolderMapper() {}

    public static FolderResponseDto toFolderResponseDto(FolderResponse folder) {
        return FolderResponseDto.builder()
                .id(UUID.fromString(folder.getId()))
                .name(folder.getName())
                .documentCount(folder.getDocumentCount())
                .build();
    }

    public static FolderDetailResponseDto toFolderDetailResponseDto(FolderDetailResponse folder, UserDetailResponse createdBy, UserDetailResponse updatedBy) {
        return FolderDetailResponseDto.builder()
                .id(UUID.fromString(folder.getId()))
                .name(folder.getName())
                .createdBy(createdBy.getUsername())
                .createdAt(LocalDateTime.parse(folder.getCreatedAt()))
                .updatedBy(updatedBy.getUsername())
                .updatedAt(LocalDateTime.parse(folder.getUpdatedAt()))
                .documentCount(folder.getDocumentCount())
                .bytes(folder.getBytes())
                .build();
    }

    public static FolderSummaryResponseDto toFolderSummaryResponseDto(FolderSummaryResponse folder) {
        return FolderSummaryResponseDto.builder()
                .id(UUID.fromString(folder.getId()))
                .name(folder.getName())
                .documentCount(folder.getDocumentCount())
                .build();
    }

    public static FoldersResponseDto toFoldersResponseDto(FoldersResponse folders) {
        LocalDateTime nextCursor = folders.getNextCursor().isEmpty() ? null : LocalDateTime.parse(folders.getNextCursor());

        List<FolderSummaryResponseDto> summaries = folders.getFoldersList().stream()
                .map(FolderMapper::toFolderSummaryResponseDto)
                .toList();

        return FoldersResponseDto.builder()
                .folders(summaries)
                .total(folders.getTotal())
                .nextCursor(nextCursor)
                .build();
    }
}
