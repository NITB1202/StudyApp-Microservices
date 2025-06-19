package com.study.apigateway.controller;

import com.study.apigateway.dto.Action.ActionResponseDto;
import com.study.apigateway.dto.Notification.request.RegisterDeviceTokenRequestDto;
import com.study.apigateway.exception.ErrorResponse;
import com.study.apigateway.service.Notification.DeviceTokenService;
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
@RequestMapping("/api/device-tokens")
public class DeviceTokenController {
    private final DeviceTokenService deviceTokenService;

    @PostMapping
    @Operation(summary = "Register a device token to receive real-time notifications.")
    @ApiResponse(responseCode = "200", description = "Register successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid request body.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<ActionResponseDto>> registerDeviceToken(@AuthenticationPrincipal UUID userId,
                                                                       @Valid @RequestBody RegisterDeviceTokenRequestDto request) {
        return deviceTokenService.registerDeviceToken(userId, request).map(ResponseEntity::ok);
    }

    @DeleteMapping
    @Operation(summary = "Remove a device token, used when log out.")
    @ApiResponse(responseCode = "200", description = "Remove successfully.")
    @ApiResponse(responseCode = "404", description = "Not found.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<ActionResponseDto>>  removeDeviceToken(@RequestParam String fcmToken) {
        return deviceTokenService.removeDeviceToken(fcmToken).map(ResponseEntity::ok);
    }
}
