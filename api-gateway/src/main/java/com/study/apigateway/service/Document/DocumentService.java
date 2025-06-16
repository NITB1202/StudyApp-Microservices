package com.study.apigateway.service.Document;

import com.study.apigateway.dto.Action.ActionResponseDto;
import com.study.apigateway.dto.Document.Document.DocumentDetailResponseDto;
import com.study.apigateway.dto.Document.Document.DocumentResponseDto;
import com.study.apigateway.dto.Document.Document.DocumentsResponseDto;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

public interface DocumentService {
    Mono<DocumentResponseDto> uploadDocument(UUID userId, UUID folderId, String name, FilePart file);
    Mono<DocumentDetailResponseDto> getDocumentById(UUID id);
    Mono<DocumentsResponseDto> getDocuments(UUID folderId, LocalDateTime cursor, int size);
    Mono<DocumentsResponseDto> searchDocumentByName(UUID folderId, String keyword, LocalDateTime cursor, int size);
    Mono<ActionResponseDto> updateDocumentName(UUID userId, UUID id, String name);
    Mono<ActionResponseDto> moveDocument(UUID userId, UUID id, UUID newFolderId);
    Mono<ActionResponseDto> deleteDocument(UUID userId, UUID id);
}
