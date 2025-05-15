package com.study.apigateway.controller;

import com.study.apigateway.dto.Action.ActionResponseDto;
import com.study.apigateway.dto.Plan.Task.request.*;
import com.study.apigateway.exception.ErrorResponse;
import com.study.apigateway.service.Plan.TaskService;
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
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService taskService;

    @PostMapping("/personal")
    @Operation(summary = "Add tasks to a personal plan.")
    @ApiResponse(responseCode = "200", description = "Add successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid request body.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<ActionResponseDto>> addTasksToPersonalPlan(@RequestParam UUID userId,
                                                                           @Valid @RequestBody AddTasksToPersonalPlanRequestDto request) {
        return taskService.addTasksToPersonalPlan(userId, request).map(ResponseEntity::ok);
    }

    @PostMapping("/team")
    @Operation(summary = "Add tasks to a team plan.")
    @ApiResponse(responseCode = "200", description = "Add successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid request body.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<ActionResponseDto>> addTasksToTeamPlan(@RequestParam UUID userId,
                                                                       @Valid @RequestBody AddTasksToTeamPlanRequestDto request) {
        return taskService.addTasksToTeamPlan(userId, request).map(ResponseEntity::ok);
    }

    @PatchMapping("/status")
    @Operation(summary = "Update statuses of multiple tasks.")
    @ApiResponse(responseCode = "200", description = "Update successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid request body.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Not found.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<ActionResponseDto>> updateTasksStatus(@RequestParam UUID userId,
                                                                     @Valid @RequestBody UpdateTasksStatusRequestDto request) {
        return taskService.updateTasksStatus(userId, request).map(ResponseEntity::ok);
    }

    @PatchMapping("/assignee")
    @Operation(summary = "Update assignees of multiple tasks.")
    @ApiResponse(responseCode = "200", description = "Update successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid request body.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Not found.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<ActionResponseDto>> getTasksAssignee(@RequestParam UUID userId,
                                                                    @Valid @RequestBody UpdateTasksAssigneeRequestDto request) {
        return taskService.updateTasksAssignee(userId, request).map(ResponseEntity::ok);
    }

    @DeleteMapping
    @Operation(summary = "Delete multiple tasks.")
    @ApiResponse(responseCode = "200", description = "Delete successfully.")
    @ApiResponse(responseCode = "404", description = "Not found.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<ActionResponseDto>> deleteTasks(@RequestParam UUID userId,
                                                               @Valid @RequestBody DeleteTasksRequestDto request) {
        return taskService.deleteTasks(userId, request).map(ResponseEntity::ok);
    }
}
