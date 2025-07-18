package com.study.apigateway.controller;

import com.study.apigateway.dto.Action.ActionResponseDto;
import com.study.apigateway.dto.Plan.Plan.request.CreatePersonalPlanRequestDto;
import com.study.apigateway.dto.Plan.Plan.request.RestorePlanRequestDto;
import com.study.apigateway.dto.Plan.Plan.request.UpdatePlanRequestDto;
import com.study.apigateway.dto.Plan.Plan.response.PlanDetailResponseDto;
import com.study.apigateway.dto.Plan.Plan.response.PlanResponseDto;
import com.study.apigateway.dto.Plan.Plan.response.PlanStatisticsResponseDto;
import com.study.apigateway.dto.Plan.Plan.response.PlanSummaryResponseDto;
import com.study.apigateway.exception.ErrorResponse;
import com.study.apigateway.service.Plan.PlanService;
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

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/plans")
public class PlanController {
    private final PlanService planService;

    @PostMapping
    @Operation(summary = "Create a new personal plan.")
    @ApiResponse(responseCode = "200", description = "Create successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid request body.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<PlanResponseDto>> createPersonalPlan(@AuthenticationPrincipal UUID userId,
                                                                    @Valid @RequestBody CreatePersonalPlanRequestDto request) {
        return planService.createPersonalPlan(userId, request).map(ResponseEntity::ok);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a plan by id.")
    @ApiResponse(responseCode = "200", description = "Get successfully.")
    @ApiResponse(responseCode = "404", description = "Not found.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<PlanDetailResponseDto>> getPlanById(@PathVariable UUID id) {
        return planService.getPlanById(id).map(ResponseEntity::ok);
    }

    @GetMapping("/date")
    @Operation(summary = "Get assigned plans for a specific date.")
    @ApiResponse(responseCode = "200", description = "Get successfully.")
    public Mono<ResponseEntity<List<PlanSummaryResponseDto>>> getAssignedPlansOnDate(@AuthenticationPrincipal UUID userId,
                                                                                     @RequestParam LocalDate date) {
        return planService.getAssignedPlansOnDate(userId, date).map(ResponseEntity::ok);
    }

    @GetMapping("/month")
    @Operation(summary = "Get dates with assigned plan deadlines in a month.")
    @ApiResponse(responseCode = "200", description = "Get successfully.")
    public Mono<ResponseEntity<List<LocalDate>>> getDatesWithAssignedPlanDeadlineInMonth(@AuthenticationPrincipal UUID userId,
                                                                                         @RequestParam int month,
                                                                                         @RequestParam int year) {
        return planService.getDatesWithAssignedPlanDeadlineInMonth(userId, month, year).map(ResponseEntity::ok);
    }

    @GetMapping("/missed")
    @Operation(summary = "Get personal missed plans.")
    @ApiResponse(responseCode = "200", description = "Get successfully.")
    public Mono<ResponseEntity<List<PlanSummaryResponseDto>>> getPersonalMissedPlans(@AuthenticationPrincipal UUID userId){
        return planService.getPersonalMissedPlans(userId).map(ResponseEntity::ok);
    }

    @PatchMapping("/{planId}")
    @Operation(summary = "Update plan's information.")
    @ApiResponse(responseCode = "200", description = "Update successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid request body.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Not found.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<PlanResponseDto>> updatePlan(@AuthenticationPrincipal UUID userId,
                                                            @PathVariable UUID planId,
                                                            @Valid @RequestBody UpdatePlanRequestDto request) {
        return planService.updatePlan(userId, planId, request).map(ResponseEntity::ok);
    }

    @DeleteMapping("/{planId}")
    @Operation(summary = "Delete a plan.")
    @ApiResponse(responseCode = "200", description = "Delete successfully.")
    @ApiResponse(responseCode = "404", description = "Not found.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<ActionResponseDto>> deletePlan(@AuthenticationPrincipal UUID userId,
                                                              @PathVariable UUID planId) {
        return planService.deletePlan(userId, planId).map(ResponseEntity::ok);
    }

    @PatchMapping("/restore/{planId}")
    @Operation(summary = "Restore a plan.")
    @ApiResponse(responseCode = "200", description = "Restore successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid request body.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Not found.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<ActionResponseDto>> restorePlan(@AuthenticationPrincipal UUID userId,
                                                               @PathVariable UUID planId,
                                                               @Valid @RequestBody RestorePlanRequestDto request){
        return planService.restorePlan(userId, planId, request).map(ResponseEntity::ok);
    }

    @GetMapping("/statistic")
    @Operation(summary = "Get weekly plan statistics.")
    @ApiResponse(responseCode = "200", description = "Get successfully.")
    public Mono<ResponseEntity<PlanStatisticsResponseDto>> getWeeklyPlanStatistics(@AuthenticationPrincipal UUID userId) {
        return planService.getWeeklyPlanStatistics(userId).map(ResponseEntity::ok);
    }
}