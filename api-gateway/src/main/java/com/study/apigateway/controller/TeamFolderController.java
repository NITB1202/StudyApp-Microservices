package com.study.apigateway.controller;

import com.study.apigateway.dto.Document.Folder.request.CreateFolderRequestDto;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/folders/team")
public class TeamFolderController {
    private final FolderService folderService;

    @PostMapping("/{teamId}")
    @Operation(summary = "Create a team folder.")
    @ApiResponse(responseCode = "200", description = "Create successfully.")
    @ApiResponse(responseCode = "200", description = "Update successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid request body.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Not found.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<FolderResponseDto>> createTeamFolder(@AuthenticationPrincipal UUID userId,
                                                                    @PathVariable UUID teamId,
                                                                    @Valid @RequestBody CreateFolderRequestDto request) {
        return folderService.createTeamFolder(userId, teamId, request).map(ResponseEntity::ok);
    }

    @GetMapping("/all")
    @Operation(summary = "Get a list of team folders.")
    @ApiResponse(responseCode = "200", description = "Get successfully.")
    public Mono<ResponseEntity<FoldersResponseDto>> getTeamFolders(@RequestParam UUID teamId,
                                                                   @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime cursor,
                                                                   @RequestParam(defaultValue = "10") int size) {
        return folderService.getTeamFolders(teamId, cursor, size).map(ResponseEntity::ok);
    }

    @GetMapping("/search")
    @Operation(summary = "Search for a team folder by name.")
    @ApiResponse(responseCode = "200", description = "Search successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid request body.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<FoldersResponseDto>> searchTeamFolderByName(@RequestParam UUID teamId,
                                                                           @RequestParam String keyword,
                                                                           @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime cursor,
                                                                           @RequestParam(defaultValue = "10") int size) {
        return folderService.searchTeamFolderByName(teamId, keyword, cursor, size).map(ResponseEntity::ok);
    }
}
