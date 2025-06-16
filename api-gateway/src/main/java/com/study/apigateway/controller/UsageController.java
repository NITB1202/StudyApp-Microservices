package com.study.apigateway.controller;

import com.study.apigateway.dto.Document.Usage.UsageResponseDto;
import com.study.apigateway.exception.ErrorResponse;
import com.study.apigateway.service.Document.UsageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/usages")
public class UsageController {
    private final UsageService usageService;

    @GetMapping("/user")
    @Operation(summary = "Get user's usage.")
    @ApiResponse(responseCode = "200", description = "Get successfully.")
    public Mono<ResponseEntity<UsageResponseDto>> getUserUsage(@RequestParam UUID userId) {
        return usageService.getUserUsage(userId).map(ResponseEntity::ok);
    }

    @GetMapping("/team")
    @Operation(summary = "Get team's usage.")
    @ApiResponse(responseCode = "200", description = "Get successfully.")
    @ApiResponse(responseCode = "404", description = "Not found.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<UsageResponseDto>> getTeamUsage(@RequestParam UUID teamId) {
        return usageService.getTeamUsage(teamId).map(ResponseEntity::ok);
    }
}
