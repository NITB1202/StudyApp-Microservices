package com.study.apigateway.controller;

import com.study.apigateway.dto.Action.ActionResponseDto;
import com.study.apigateway.dto.Notification.request.DeleteNotificationsRequestDto;
import com.study.apigateway.dto.Notification.request.MarkNotificationsAsReadRequestDto;
import com.study.apigateway.dto.Notification.response.NotificationsResponseDto;
import com.study.apigateway.dto.Notification.response.UnreadNotificationCountResponseDto;
import com.study.apigateway.exception.ErrorResponse;
import com.study.apigateway.service.Notification.NotificationService;
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
@RequestMapping("/api/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping
    @Operation(summary = "Get a list of notifications.")
    @ApiResponse(responseCode = "200", description = "Get successfully.")
    public Mono<ResponseEntity<NotificationsResponseDto>> getNotifications(@AuthenticationPrincipal UUID userId,
                                                                           @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime cursor,
                                                                           @RequestParam(defaultValue = "10") int size) {
        return notificationService.getNotifications(userId, cursor, size).map(ResponseEntity::ok);
    }

    @GetMapping("/count/unread")
    @Operation(summary = "Get unread notifications count.")
    @ApiResponse(responseCode = "200", description = "Get successfully.")
    public Mono<ResponseEntity<UnreadNotificationCountResponseDto>> getUnreadNotificationCount(@AuthenticationPrincipal UUID userId) {
        return notificationService.getUnreadNotificationCount(userId).map(ResponseEntity::ok);
    }

    @PostMapping("/mark")
    @Operation(summary = "Mark selected notifications as read.")
    @ApiResponse(responseCode = "200", description = "Mark successfully.")
    public Mono<ResponseEntity<ActionResponseDto>> markNotificationsAsRead(@Valid @RequestBody MarkNotificationsAsReadRequestDto request) {
        return notificationService.markNotificationsAsRead(request).map(ResponseEntity::ok);
    }

    @PostMapping("/mark/all")
    @Operation(summary = "Mark all notifications as read.")
    @ApiResponse(responseCode = "200", description = "Mark successfully.")
    public Mono<ResponseEntity<ActionResponseDto>> markAllNotificationsAsRead(@AuthenticationPrincipal UUID userId) {
        return notificationService.markAllNotificationsAsRead(userId).map(ResponseEntity::ok);
    }

    @DeleteMapping
    @Operation(summary = "Delete the selected notifications.")
    @ApiResponse(responseCode = "200", description = "Delete successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid request body.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<ActionResponseDto>> deleteNotifications(@AuthenticationPrincipal UUID userId,
                                                                       @Valid @RequestBody DeleteNotificationsRequestDto request) {
        return notificationService.deleteNotifications(userId, request).map(ResponseEntity::ok);
    }

    @DeleteMapping("/all")
    @Operation(summary = "Delete all notifications.")
    @ApiResponse(responseCode = "200", description = "Delete successfully.")
    public Mono<ResponseEntity<ActionResponseDto>> deleteAllNotifications(@AuthenticationPrincipal UUID userId) {
        return notificationService.deleteAllNotifications(userId).map(ResponseEntity::ok);
    }
}
