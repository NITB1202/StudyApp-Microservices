package com.study.apigateway.controller;

import com.study.apigateway.dto.Plan.Plan.request.CreatePersonalPlanRequestDto;
import com.study.apigateway.dto.Plan.Plan.request.CreateTeamPlanRequestDto;
import com.study.apigateway.dto.Plan.Plan.response.PlanDetailResponseDto;
import com.study.apigateway.dto.Plan.Plan.response.PlanResponseDto;
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

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/plans")
public class PlanController {
    private final PlanService planService;

    @PostMapping("/personal")
    @Operation(summary = "Create a new personal plan.")
    @ApiResponse(responseCode = "200", description = "Create successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid request body.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<PlanResponseDto>> createPersonalPlan(@RequestParam UUID userId,
                                                                    @Valid @RequestBody CreatePersonalPlanRequestDto request) {
        return planService.createPersonalPlan(userId, request).map(ResponseEntity::ok);
    }

    @PostMapping("/team")
    @Operation(summary = "Create a new team plan.")
    @ApiResponse(responseCode = "200", description = "Create successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid request body.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<PlanResponseDto>> createTeamPlan(@RequestParam UUID userId,
                                                                @Valid @RequestBody CreateTeamPlanRequestDto request) {
        return planService.createTeamPlan(userId, request).map(ResponseEntity::ok);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a plan by id.")
    @ApiResponse(responseCode = "200", description = "Get successfully.")
    @ApiResponse(responseCode = "404", description = "Not found.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<PlanDetailResponseDto>> getPlanById(@PathVariable UUID id) {
        return planService.getPlanById(id).map(ResponseEntity::ok);
    }
}
