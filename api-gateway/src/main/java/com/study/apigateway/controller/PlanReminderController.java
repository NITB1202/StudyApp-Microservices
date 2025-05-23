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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reminders")
public class PlanReminderController {
    private final PlanReminderService planReminderService;

    @PostMapping
    @Operation(summary = "Add reminders to a plan.")
    @ApiResponse(responseCode = "200", description = "Add successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid request body.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<ActionResponseDto>> addPlanReminders(@Valid @RequestBody AddPlanRemindersRequestDto request) {
        return planReminderService.addPlanReminders(request).map(ResponseEntity::ok);
    }

    @PatchMapping
    @Operation(summary = "Update reminders.")
    @ApiResponse(responseCode = "200", description = "Update successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid request body.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Not found.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<ActionResponseDto>> updatePlanReminders(@Valid @RequestBody UpdatePlanRemindersRequestDto request) {
        return planReminderService.updatePlanReminders(request).map(ResponseEntity::ok);
    }

    @DeleteMapping
    @Operation(summary = "Delete reminders.")
    @ApiResponse(responseCode = "200", description = "Delete successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid request body.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Not found.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<ActionResponseDto>> deletePlanReminders(@Valid @RequestBody DeletePlanRemindersRequestDto request) {
        return planReminderService.deletePlanReminders(request).map(ResponseEntity::ok);
    }
}
