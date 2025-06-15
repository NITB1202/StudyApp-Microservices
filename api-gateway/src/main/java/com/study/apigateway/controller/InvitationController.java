package com.study.apigateway.controller;

import com.study.apigateway.dto.Action.ActionResponseDto;
import com.study.apigateway.dto.Notification.response.InvitationsResponseDto;
import com.study.apigateway.exception.ErrorResponse;
import com.study.apigateway.service.Notification.InvitationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/invitations")
public class InvitationController {
    private final InvitationService invitationService;

    @GetMapping
    @Operation(summary = "Get a list of invitations.")
    @ApiResponse(responseCode = "200", description = "Get successfully.")
    public Mono<ResponseEntity<InvitationsResponseDto>> getInvitations(@RequestParam UUID userId,
                                                                       @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime cursor,
                                                                       @RequestParam(defaultValue = "10") int size) {
        return invitationService.getInvitations(userId, cursor, size).map(ResponseEntity::ok);
    }

    @PostMapping("/{id}")
    @Operation(summary = "Reply to the invitation.")
    @ApiResponse(responseCode = "200", description = "Reply successfully.")
    @ApiResponse(responseCode = "404", description = "Not found.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<ActionResponseDto>> replyToInvitation(@PathVariable UUID id, @RequestParam UUID userId, @RequestParam boolean accept) {
        return invitationService.replyToInvitation(id, userId, accept).map(ResponseEntity::ok);
    }
}
