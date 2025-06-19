package com.study.apigateway.controller;

import com.study.apigateway.dto.Notification.request.UpdateTeamNotificationSettingsRequestDto;
import com.study.apigateway.dto.Notification.response.TeamNotificationSettingsResponseDto;
import com.study.apigateway.exception.ErrorResponse;
import com.study.apigateway.service.Notification.TeamNotificationSettingsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/settings")
public class TeamNotificationSettingsController {
    private final TeamNotificationSettingsService settingsService;

    @GetMapping("/{teamId}")
    @Operation(summary = "Get the team's notification settings.")
    @ApiResponse(responseCode = "200", description = "Get successfully.")
    @ApiResponse(responseCode = "404", description = "Not found.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<TeamNotificationSettingsResponseDto>> getTeamNotificationSettings(@AuthenticationPrincipal UUID userId, @PathVariable UUID teamId) {
        return settingsService.getTeamNotificationSettings(userId, teamId).map(ResponseEntity::ok);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update the team's notification settings.")
    @ApiResponse(responseCode = "200", description = "Update successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid request body.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Not found.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<TeamNotificationSettingsResponseDto>> updateTeamNotificationSettings(@PathVariable UUID id,
                                                                                                    @Valid @RequestBody UpdateTeamNotificationSettingsRequestDto request) {
        return settingsService.updateTeamNotificationSettings(id, request).map(ResponseEntity::ok);
    }
}
