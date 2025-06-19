package com.study.apigateway.controller;

import com.study.apigateway.dto.Action.ActionResponseDto;
import com.study.apigateway.dto.Action.StringWrapper;
import com.study.apigateway.dto.Chat.request.MarkMessagesAsReadRequestDto;
import com.study.apigateway.dto.Chat.request.SendMessageRequestDto;
import com.study.apigateway.dto.Chat.request.UpdateMessageRequestDto;
import com.study.apigateway.dto.Chat.response.MessagesResponseDto;
import com.study.apigateway.dto.Chat.response.UnreadMessageCountResponseDto;
import com.study.apigateway.exception.ErrorResponse;
import com.study.apigateway.service.Chat.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
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
@RequestMapping("/api/chat")
public class ChatServiceController {
    private final ChatService chatService;

    @GetMapping("/ws")
    @Operation(summary = "Get available web socket url (connect web socket to receive real-time message).")
    public Mono<ResponseEntity<StringWrapper>> getWebsocketUrl() {
        String url = "ws://localhost:8086/ws/chat?userId=<userId>&teamId=<teamId>";

        StringWrapper wrapper = StringWrapper.builder()
                .content(url)
                .build();

        return Mono.just(ResponseEntity.ok(wrapper));
    }

    @PostMapping("/{teamId}")
    @Operation(summary = "Send a message to the team.")
    @ApiResponse(responseCode = "200", description = "Send successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid request body.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<ActionResponseDto>> sendMessage(@AuthenticationPrincipal UUID userId,
                                                               @PathVariable UUID teamId,
                                                               @Valid @RequestBody SendMessageRequestDto dto) {
        return chatService.sendMessage(userId, teamId, dto).map(ResponseEntity::ok);
    }

    @PostMapping(value = "/{teamId}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Send an image to the team.")
    @ApiResponse(responseCode = "200", description = "Send successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid request body.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<ActionResponseDto>> sendImageMessage(@AuthenticationPrincipal UUID userId,
                                                                    @PathVariable UUID teamId,
                                                                    @RequestPart("file") FilePart file) {
        return chatService.sendImageMessage(userId, teamId, file).map(ResponseEntity::ok);
    }

    @GetMapping("{teamId}/unread")
    @Operation(summary = "Get unread messages count for team chat notification.")
    @ApiResponse(responseCode = "200", description = "Get successfully.")
    public Mono<ResponseEntity<UnreadMessageCountResponseDto>> getUnreadMessageCount(@AuthenticationPrincipal UUID userId, @PathVariable UUID teamId) {
        return chatService.getUnreadMessageCount(userId, teamId).map(ResponseEntity::ok);
    }

    @GetMapping("/all")
    @Operation(summary = "Get a list of messages.")
    @ApiResponse(responseCode = "200", description = "Get successfully.")
    public Mono<ResponseEntity<MessagesResponseDto>> getMessages(@RequestParam UUID teamId,
                                                                 @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime cursor,
                                                                 @RequestParam(defaultValue = "10") int size) {
        return chatService.getMessages(teamId, cursor, size).map(ResponseEntity::ok);
    }

    @PutMapping("/{messageId}")
    @Operation(summary = "Update a message.")
    @ApiResponse(responseCode = "200", description = "Update successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid request body.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Not found.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<ActionResponseDto>> updateMessage(@AuthenticationPrincipal UUID userId,
                                                                 @PathVariable UUID messageId,
                                                                 @Valid @RequestBody UpdateMessageRequestDto dto) {
        return chatService.updateMessage(userId, messageId, dto).map(ResponseEntity::ok);
    }


    @PatchMapping("/{teamId}")
    @Operation(summary = "Mark the on-screen messages as read.")
    @ApiResponse(responseCode = "200", description = "Mark successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid request body.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<ActionResponseDto>> markMessagesAsRead(@AuthenticationPrincipal UUID userId,
                                                                      @PathVariable UUID teamId,
                                                                      @Valid @RequestBody MarkMessagesAsReadRequestDto dto) {
        return chatService.markMessagesAsRead(userId, teamId, dto).map(ResponseEntity::ok);
    }

    @DeleteMapping("/{messageId}")
    @Operation(summary = "Delete a message.")
    @ApiResponse(responseCode = "200", description = "Delete successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid request body.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Not found.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<ActionResponseDto>> deleteMessage(@AuthenticationPrincipal UUID userId, @PathVariable UUID messageId) {
        return chatService.deleteMessage(userId, messageId).map(ResponseEntity::ok);
    }
}
