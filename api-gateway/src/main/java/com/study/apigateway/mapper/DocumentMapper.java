package com.study.apigateway.mapper;

import com.study.apigateway.dto.Document.Document.DocumentDetailResponseDto;
import com.study.apigateway.dto.Document.Document.DocumentResponseDto;
import com.study.apigateway.dto.Document.Document.DocumentsResponseDto;
import com.study.documentservice.grpc.DocumentDetailResponse;
import com.study.documentservice.grpc.DocumentResponse;
import com.study.documentservice.grpc.DocumentsResponse;
import com.study.userservice.grpc.UserDetailResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class DocumentMapper {
    private DocumentMapper() {}

    public static DocumentResponseDto toDocumentResponseDto(DocumentResponse document) {
        return DocumentResponseDto.builder()
                .id(UUID.fromString(document.getId()))
                .name(document.getName())
                .url(document.getUrl())
                .updatedAt(LocalDateTime.parse(document.getUpdatedAt()))
                .bytes(document.getBytes())
                .build();
    }

    public static DocumentDetailResponseDto toDocumentDetailResponseDto(DocumentDetailResponse document, UserDetailResponse createdBy, UserDetailResponse updatedBy) {
        return DocumentDetailResponseDto.builder()
                .id(UUID.fromString(document.getId()))
                .name(document.getName())
                .createdBy(createdBy.getUsername())
                .createdAt(LocalDateTime.parse(document.getCreatedAt()))
                .updatedBy(updatedBy.getUsername())
                .updatedAt(LocalDateTime.parse(document.getUpdatedAt()))
                .bytes(document.getBytes())
                .url(document.getUrl())
                .build();
    }

    public static DocumentsResponseDto toDocumentsResponseDto(DocumentsResponse documents) {
        LocalDateTime nextCursor = documents.getNextCursor().isEmpty() ? null : LocalDateTime.parse(documents.getNextCursor());

        List<DocumentResponseDto> dto = documents.getDocumentsList().stream()
                .map(DocumentMapper::toDocumentResponseDto)
                .toList();

        return DocumentsResponseDto.builder()
                .documents(dto)
                .total(documents.getTotal())
                .nextCursor(nextCursor)
                .build();
    }
}
