package com.study.apigateway.controller;


import com.study.apigateway.dto.Plan.Plan.request.CreateTeamPlanRequestDto;
import com.study.apigateway.dto.Plan.Plan.response.PlanResponseDto;
import com.study.apigateway.dto.Plan.Plan.response.TeamPlanSummaryResponseDto;
import com.study.apigateway.exception.ErrorResponse;
import com.study.apigateway.service.Plan.PlanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/plans/team")
public class TeamPlanController {
    private final PlanService planService;

    @PostMapping
    @Operation(summary = "Create a new team plan.")
    @ApiResponse(responseCode = "200", description = "Create successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid request body.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<PlanResponseDto>> createTeamPlan(@RequestParam UUID userId,
                                                                @Valid @RequestBody CreateTeamPlanRequestDto request) {
        return planService.createTeamPlan(userId, request).map(ResponseEntity::ok);
    }

    @GetMapping("/date")
    @Operation(summary = "Get team plans for a specific date.")
    @ApiResponse(responseCode = "200", description = "Get successfully.")
    public Mono<ResponseEntity<List<TeamPlanSummaryResponseDto>>> getTeamPlansOnDate(@RequestParam UUID userId,
                                                                                     @RequestParam UUID teamId,
                                                                                     @RequestParam LocalDate date) {
        return planService.getTeamPlansOnDate(userId, teamId, date).map(ResponseEntity::ok);
    }

    @GetMapping("/month")
    @Operation(summary = "Get dates with team plan deadlines in a month.")
    @ApiResponse(responseCode = "200", description = "Get successfully.")
    public Mono<ResponseEntity<List<LocalDate>>> getDatesWithTeamPlanDeadlineInMonth(@RequestParam UUID userId,
                                                                                     @RequestParam UUID teamId,
                                                                                     @RequestParam int month,
                                                                                     @RequestParam int year) {
        return planService.getDatesWithTeamPlanDeadlineInMonth(userId, teamId, month, year).map(ResponseEntity::ok);
    }

    @GetMapping("/missed")
    @Operation(summary = "Get team missed plans.")
    @ApiResponse(responseCode = "200", description = "Get successfully.")
    public Mono<ResponseEntity<List<TeamPlanSummaryResponseDto>>> getTeamMissedPlans(@RequestParam UUID userId,
                                                                                     @RequestParam UUID teamId){
        return planService.getTeamMissedPlans(userId, teamId).map(ResponseEntity::ok);
    }
}
