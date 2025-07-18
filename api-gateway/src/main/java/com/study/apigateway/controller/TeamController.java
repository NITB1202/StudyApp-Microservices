package com.study.apigateway.controller;

import com.study.apigateway.dto.Action.ActionResponseDto;
import com.study.apigateway.dto.Team.Team.request.CreateTeamRequestDto;
import com.study.apigateway.dto.Team.Team.request.UpdateTeamRequestDto;
import com.study.apigateway.dto.Team.Team.response.ListTeamResponseDto;
import com.study.apigateway.dto.Team.Team.response.TeamDetailResponseDto;
import com.study.apigateway.dto.Team.Team.response.TeamProfileResponseDto;
import com.study.apigateway.dto.Team.Team.response.TeamResponseDto;
import com.study.apigateway.exception.ErrorResponse;
import com.study.apigateway.service.Team.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/teams")
public class TeamController {
    private final TeamService teamService;

    @PostMapping
    @Operation(summary = "Create a new team.")
    @ApiResponse(responseCode = "200", description = "Create successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid request body.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<TeamResponseDto>> createTeam(@AuthenticationPrincipal UUID userId,
                                                            @Valid @RequestBody CreateTeamRequestDto request){
        return teamService.createTeam(userId, request).map(ResponseEntity::ok);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get team's information by id.")
    @ApiResponse(responseCode = "200", description = "Get successfully")
    @ApiResponse(responseCode = "404", description = "Not found.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<TeamDetailResponseDto>> getTeamById(@PathVariable UUID id){
        return teamService.getTeamById(id).map(ResponseEntity::ok);
    }

    @GetMapping("/code/{teamCode}")
    @Operation(summary = "Get team's information by team code.")
    @ApiResponse(responseCode = "200", description = "Get successfully")
    @ApiResponse(responseCode = "404", description = "Not found.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<TeamProfileResponseDto>> getTeamByTeamCode(@PathVariable String teamCode){
        return teamService.getTeamByTeamCode(teamCode).map(ResponseEntity::ok);
    }

    @GetMapping("/all")
    @Operation(summary = "Get part of the user's teams.")
    @ApiResponse(responseCode = "200", description = "Get successfully.")
    @ApiResponse(responseCode = "404", description = "Not found.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<ListTeamResponseDto>> getUserTeams(@AuthenticationPrincipal UUID userId,
                                                                  @RequestParam(required = false) String cursor,
                                                                  @RequestParam(defaultValue = "10") int size){
        return teamService.getUserTeams(userId, cursor,size).map(ResponseEntity::ok);
    }

    @GetMapping("/search")
    @Operation(summary = "Search for user's teams by name.")
    @ApiResponse(responseCode = "200", description = "Search successfully.")
    @ApiResponse(responseCode = "404", description = "Not found.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<ListTeamResponseDto>> searchTeams(@AuthenticationPrincipal UUID userId,
                                                                 @RequestParam String keyword,
                                                                 @RequestParam(required = false) String cursor,
                                                                 @RequestParam(defaultValue = "10") int size ){
        return teamService.searchUserTeamByName(userId, keyword, cursor, size).map(ResponseEntity::ok);
    }

    @PatchMapping("/{teamId}")
    @Operation(summary = "Update team's information.")
    @ApiResponse(responseCode = "200", description = "Update successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid request body.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Not found.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<TeamResponseDto>> updateTeam(@PathVariable UUID teamId,
                                                            @AuthenticationPrincipal UUID userId,
                                                            @Valid @RequestBody UpdateTeamRequestDto request){
        return teamService.updateTeam(userId, teamId, request).map(ResponseEntity::ok);
    }

    @PatchMapping("/reset/{teamId}")
    @Operation(summary = "Reset team code.")
    @ApiResponse(responseCode = "200", description = "Reset successfully.")
    @ApiResponse(responseCode = "404", description = "Not found.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<ActionResponseDto>> resetTeamCode(@PathVariable UUID teamId,
                                                                 @AuthenticationPrincipal UUID userId) {
        return teamService.resetTeamCode(userId, teamId).map(ResponseEntity::ok);
    }

    @DeleteMapping("/{teamId}")
    @Operation(summary = "Delete a team.")
    @ApiResponse(responseCode = "200", description = "Delete successfully.")
    @ApiResponse(responseCode = "404", description = "Not found.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<ActionResponseDto>> deleteTeam(@PathVariable UUID teamId,
                                                              @AuthenticationPrincipal UUID userId){
        return teamService.deleteTeam(teamId, userId).map(ResponseEntity::ok);
    }

    @PostMapping(value = "/avatar/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload team's avatar.")
    @ApiResponse(responseCode = "200", description = "Upload successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid request body.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Not found.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<ActionResponseDto>> uploadTeamAvatar(@AuthenticationPrincipal UUID userId,
                                                                    @PathVariable UUID id,
                                                                    @RequestPart("file") FilePart file) {
        return teamService.uploadTeamAvatar(userId, id, file).map(ResponseEntity::ok);
    }
}
