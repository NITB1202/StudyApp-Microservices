package com.study.apigateway.controller;

import com.study.apigateway.dto.Action.ActionResponseDto;
import com.study.apigateway.dto.Document.Document.DocumentDetailResponseDto;
import com.study.apigateway.dto.Document.Document.DocumentResponseDto;
import com.study.apigateway.dto.Document.Document.DocumentsResponseDto;
import com.study.apigateway.exception.ErrorResponse;
import com.study.apigateway.service.Document.DocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/documents")
public class DocumentController {
    private final DocumentService documentService;

    @PostMapping(value = "/{folderId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload a document to the folder.")
    @ApiResponse(responseCode = "200", description = "Upload successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid request body.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<DocumentResponseDto>> uploadDocument(@AuthenticationPrincipal UUID userId,
                                                                    @PathVariable UUID folderId,
                                                                    @RequestParam String name,
                                                                    @RequestPart("file") FilePart file) {
        return documentService.uploadDocument(userId, folderId, name, file).map(ResponseEntity::ok);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get the details of a document.")
    @ApiResponse(responseCode = "200", description = "Get successfully.")
    @ApiResponse(responseCode = "404", description = "Not found.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<DocumentDetailResponseDto>> getDocumentById(@PathVariable UUID id) {
        return documentService.getDocumentById(id).map(ResponseEntity::ok);
    }

    @GetMapping("/all")
    @Operation(summary = "Get a list of documents.")
    @ApiResponse(responseCode = "200", description = "Get successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid request body.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<DocumentsResponseDto>> getDocuments(@RequestParam UUID folderId,
                                                                   @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime cursor,
                                                                   @RequestParam(defaultValue = "10") int size) {
        return documentService.getDocuments(folderId, cursor, size).map(ResponseEntity::ok);
    }

    @GetMapping("/search")
    @Operation(summary = "Search for a document by name.")
    @ApiResponse(responseCode = "200", description = "Search successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid request body.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<DocumentsResponseDto>> searchDocumentByName(@RequestParam UUID folderId,
                                                                           @RequestParam String keyword,
                                                                           @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime cursor,
                                                                           @RequestParam(defaultValue = "10") int size) {
        return documentService.searchDocumentByName(folderId, keyword, cursor, size).map(ResponseEntity::ok);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update the document's name.")
    @ApiResponse(responseCode = "200", description = "Update successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid request body.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Not found.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<ActionResponseDto>> updateDocumentName(@AuthenticationPrincipal UUID userId,
                                                                      @PathVariable UUID id,
                                                                      @RequestParam String name) {
        return documentService.updateDocumentName(userId, id, name).map(ResponseEntity::ok);
    }

    @PatchMapping("/move/{id}")
    @Operation(summary = "Move a document to the new folder.")
    @ApiResponse(responseCode = "200", description = "Move successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid request body.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Not found.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<ActionResponseDto>> moveDocument(@AuthenticationPrincipal UUID userId,
                                                                @PathVariable UUID id,
                                                                @RequestParam UUID newFolderId) {
        return documentService.moveDocument(userId, id, newFolderId).map(ResponseEntity::ok);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a document.")
    @ApiResponse(responseCode = "200", description = "Delete successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid request body.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Not found.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<ActionResponseDto>> deleteDocument(@AuthenticationPrincipal UUID userId,
                                                                  @PathVariable UUID id) {
        return documentService.deleteDocument(userId, id).map(ResponseEntity::ok);
    }
}
