package com.study.apigateway.controller;

import com.study.apigateway.dto.Action.ActionResponseDto;
import com.study.apigateway.dto.Document.Folder.request.CreateFolderRequestDto;
import com.study.apigateway.dto.Document.Folder.response.FolderDetailResponseDto;
import com.study.apigateway.dto.Document.Folder.response.FolderResponseDto;
import com.study.apigateway.dto.Document.Folder.response.FoldersResponseDto;
import com.study.apigateway.exception.ErrorResponse;
import com.study.apigateway.service.Document.FolderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/folders")
public class FolderController {
    private final FolderService folderService;

    @PostMapping
    @Operation(summary = "Create a personal folder.")
    @ApiResponse(responseCode = "200", description = "Create successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid request body.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<FolderResponseDto>> createPersonalFolder(@RequestParam UUID userId,
                                                                        @Valid @RequestBody CreateFolderRequestDto request) {
        return folderService.createPersonalFolder(userId, request).map(ResponseEntity::ok);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get the details of a folder.")
    @ApiResponse(responseCode = "200", description = "Get successfully.")
    @ApiResponse(responseCode = "404", description = "Not found.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<FolderDetailResponseDto>> getFolderById(@PathVariable UUID id) {
        return folderService.getFolderById(id).map(ResponseEntity::ok);
    }

    @GetMapping("/all")
    @Operation(summary = "Get a list of personal folders.")
    @ApiResponse(responseCode = "200", description = "Get successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid request body.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<FoldersResponseDto>> getPersonalFolders(@RequestParam UUID userId,
                                                                       @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime cursor,
                                                                       @RequestParam(defaultValue = "10") int size) {
        return folderService.getPersonalFolders(userId, cursor, size).map(ResponseEntity::ok);
    }

    @GetMapping("/search")
    @Operation(summary = "Search for a personal folder by name.")
    @ApiResponse(responseCode = "200", description = "Search successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid request body.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<FoldersResponseDto>> searchPersonalFolderByName(@RequestParam UUID userId,
                                                                               @RequestParam String keyword,
                                                                               @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime cursor,
                                                                               @RequestParam(defaultValue = "10") int size) {
        return folderService.searchPersonalFolderByName(userId, keyword, cursor, size).map(ResponseEntity::ok);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update a folder name.")
    @ApiResponse(responseCode = "200", description = "Update successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid request body.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Not found.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<ActionResponseDto>> updateFolderName(@RequestParam UUID userId,
                                                                    @PathVariable UUID id,
                                                                    @RequestParam String name) {
        return folderService.updateFolderName(userId, id, name).map(ResponseEntity::ok);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a folder.")
    @ApiResponse(responseCode = "200", description = "Delete successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid request body.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Not found.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<ActionResponseDto>> deleteFolder(@RequestParam UUID userId, @PathVariable UUID id) {
        return folderService.deleteFolder(userId, id).map(ResponseEntity::ok);
    }
}
