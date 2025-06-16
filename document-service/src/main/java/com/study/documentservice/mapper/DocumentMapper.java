package com.study.documentservice.mapper;

import com.study.documentservice.entity.Document;
import com.study.documentservice.grpc.DocumentDetailResponse;
import com.study.documentservice.grpc.DocumentResponse;
import com.study.documentservice.grpc.DocumentsResponse;

import javax.print.Doc;
import java.util.ArrayList;
import java.util.List;

public class DocumentMapper {
    private DocumentMapper() {}

    public static DocumentResponse toDocumentResponse(Document document) {
        return DocumentResponse.newBuilder()
                .setId(document.getId().toString())
                .setName(document.getName())
                .setUrl(document.getUrl())
                .setUpdatedAt(document.getUpdatedAt().toString())
                .setBytes(document.getBytes())
                .build();
    }

    public static DocumentDetailResponse toDocumentDetailResponse(Document document) {
        return DocumentDetailResponse.newBuilder()
                .setId(document.getId().toString())
                .setName(document.getName())
                .setCreatedBy(document.getCreatedBy().toString())
                .setCreatedAt(document.getCreatedAt().toString())
                .setUpdatedBy(document.getUpdatedBy().toString())
                .setUpdatedAt(document.getUpdatedAt().toString())
                .setBytes(document.getBytes())
                .setUrl(document.getUrl())
                .build();
    }

    public static DocumentsResponse toDocumentsResponse(List<Document> documents, long total, String nextCursor) {
        List<DocumentResponse> documentResponses = documents.stream()
                .map(DocumentMapper::toDocumentResponse)
                .toList();

        return DocumentsResponse.newBuilder()
                .addAllDocuments(documentResponses)
                .setTotal(total)
                .setNextCursor(nextCursor)
                .build();
    }
}
