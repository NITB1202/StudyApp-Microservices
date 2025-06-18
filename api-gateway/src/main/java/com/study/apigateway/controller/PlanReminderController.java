package com.study.apigateway.controller;

import com.study.apigateway.dto.Action.ActionResponseDto;
import com.study.apigateway.dto.Plan.Reminder.request.AddPlanRemindersRequestDto;
import com.study.apigateway.dto.Plan.Reminder.request.DeletePlanRemindersRequestDto;
import com.study.apigateway.dto.Plan.Reminder.request.UpdatePlanRemindersRequestDto;
import com.study.apigateway.exception.ErrorResponse;
import com.study.apigateway.service.Plan.PlanReminderService;
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
@RequestMapping("/api/reminders")
public class PlanReminderController {
    private final PlanReminderService planReminderService;

    @PostMapping("/{planId}")
    @Operation(summary = "Add reminders to a plan.")
    @ApiResponse(responseCode = "200", description = "Add successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid request body.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<ActionResponseDto>> addPlanReminders(@RequestParam UUID userId,
                                                                    @PathVariable UUID planId,
                                                                    @Valid @RequestBody AddPlanRemindersRequestDto request) {
        return planReminderService.addPlanReminders(userId, planId, request).map(ResponseEntity::ok);
    }

    @PatchMapping("/{planId}")
    @Operation(summary = "Update reminders.")
    @ApiResponse(responseCode = "200", description = "Update successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid request body.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Not found.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<ActionResponseDto>> updatePlanReminders(@RequestParam UUID userId,
                                                                       @PathVariable UUID planId,
                                                                       @Valid @RequestBody UpdatePlanRemindersRequestDto request) {
        return planReminderService.updatePlanReminders(userId, planId, request).map(ResponseEntity::ok);
    }

    @DeleteMapping("/{planId}")
    @Operation(summary = "Delete reminders.")
    @ApiResponse(responseCode = "200", description = "Delete successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid request body.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Not found.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<ActionResponseDto>> deletePlanReminders(@RequestParam UUID userId,
                                                                       @PathVariable UUID planId,
                                                                       @Valid @RequestBody DeletePlanRemindersRequestDto request) {
        return planReminderService.deletePlanReminders(userId, planId, request).map(ResponseEntity::ok);
    }
}
