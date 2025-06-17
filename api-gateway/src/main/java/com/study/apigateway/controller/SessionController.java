package com.study.apigateway.controller;

import com.study.apigateway.dto.Session.request.SaveSessionRequestDto;
import com.study.apigateway.dto.Session.response.SessionResponseDto;
import com.study.apigateway.dto.Session.response.SessionStatisticsResponseDto;
import com.study.apigateway.exception.ErrorResponse;
import com.study.apigateway.service.Session.SessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sessions")
public class SessionController {
    private final SessionService sessionService;

    @PostMapping
    @Operation(summary = "Save a study session.")
    @ApiResponse(responseCode = "200", description = "Save successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid request body.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<SessionResponseDto>> saveSession(@RequestParam UUID userId,
                                                                @Valid @RequestBody SaveSessionRequestDto request) {
        return sessionService.saveSession(userId, request).map(ResponseEntity::ok);
    }

    @GetMapping
    @Operation(summary = "Get the study session's weekly statistics.")
    @ApiResponse(responseCode = "200", description = "Get successfully.")
    public Mono<ResponseEntity<SessionStatisticsResponseDto>> getSessionWeeklyStatistics(@RequestParam UUID userId) {
        return sessionService.getSessionWeeklyStatistics(userId).map(ResponseEntity::ok);
    }
}
