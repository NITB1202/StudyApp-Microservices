package com.study.chatservice.controller;


import com.study.chatservice.dto.request.MarkMessagesAsReadRequestDto;
import com.study.chatservice.dto.request.SendMessageRequestDto;
import com.study.chatservice.dto.request.UpdateMessageRequestDto;
import com.study.chatservice.service.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatWebSocketController {
    private final ChatService chatService;

    @PostMapping("/{teamId}")
    public ResponseEntity<Void> sendMessage(@RequestParam UUID userId,
                                            @PathVariable UUID teamId,
                                            @Valid @RequestBody SendMessageRequestDto request) {
        chatService.sendMessage(userId, teamId, request);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/{teamId}/image", consumes = "multipart/form-data")
    public ResponseEntity<Void> sendImageMessage(@RequestParam UUID userId,
                                                 @PathVariable UUID teamId,
                                                 @RequestParam("file") MultipartFile file) {
        chatService.sendImageMessage(userId, teamId, file);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{messageId}")
    public ResponseEntity<Void> updateMessage(@RequestParam UUID userId,
                                              @PathVariable UUID messageId,
                                              @Valid @RequestBody UpdateMessageRequestDto request) {
        chatService.updateMessage(userId, messageId, request);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{teamId}")
    public ResponseEntity<Void> markMessagesAsRead(@RequestParam UUID userId,
                                                   @PathVariable UUID teamId,
                                                   @Valid @RequestBody MarkMessagesAsReadRequestDto request) {
        chatService.markMessagesAsRead(userId, teamId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> deleteMessage(@RequestParam UUID userId, @PathVariable UUID messageId) {
        chatService.deleteMessage(userId, messageId);
        return ResponseEntity.ok().build();
    }
}
