package com.study.apigateway.controller;

import com.study.apigateway.dto.Action.ActionResponseDto;
import com.study.apigateway.dto.Notification.request.CreateInvitationRequestDto;
import com.study.apigateway.dto.Team.request.RemoveTeamMemberRequestDto;
import com.study.apigateway.dto.Team.request.UpdateMemberRoleRequestDto;
import com.study.apigateway.dto.Team.response.ListTeamMemberResponseDto;
import com.study.apigateway.dto.Team.response.TeamUserProfileResponseDto;
import com.study.apigateway.exception.ErrorResponse;
import com.study.apigateway.service.Team.MemberService;
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
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/invitation")
    @Operation(summary = "Invite a user to the team.")
    @ApiResponse(responseCode = "200", description = "Invite successfully.")
    @ApiResponse(responseCode = "404", description = "Not found.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<ActionResponseDto>> createInvitation(@RequestParam UUID userId,
                                                                    @Valid @RequestBody CreateInvitationRequestDto request){
        return memberService.createInvitation(userId, request).map(ResponseEntity::ok);
    }

    @PostMapping
    @Operation(summary = "Join a team by team code.")
    @ApiResponse(responseCode = "200", description = "Join successfully.")
    @ApiResponse(responseCode = "404", description = "Not found.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<ActionResponseDto>> joinTeam(@RequestParam UUID userId,
                                                            @RequestParam String teamCode){
        return memberService.joinTeam(userId, teamCode).map(ResponseEntity::ok);
    }

    @GetMapping
    @Operation(summary = "Get user's information in the team.")
    @ApiResponse(responseCode = "200", description = "Get successfully.")
    @ApiResponse(responseCode = "404", description = "Not found.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<TeamUserProfileResponseDto>> getUserInTeam(@RequestParam UUID userId,
                                                                          @RequestParam UUID teamId){
        return memberService.getUserInTeam(userId, teamId).map(ResponseEntity::ok);
    }

    @GetMapping("/list")
    @Operation(summary = "Get a list of team members.")
    @ApiResponse(responseCode = "200", description = "Get successfully.")
    public Mono<ResponseEntity<ListTeamMemberResponseDto>> getTeamMembers(@RequestParam UUID teamId,
                                                                          @RequestParam(required = false) String cursor,
                                                                          @RequestParam(defaultValue = "10") int size){
        return memberService.getTeamMembers(teamId, cursor, size).map(ResponseEntity::ok);
    }

    @GetMapping("/search")
    @Operation(summary = "Search for a member by username.")
    @ApiResponse(responseCode = "200", description = "Search successfully.")
    public Mono<ResponseEntity<ListTeamMemberResponseDto>> searchTeamMembersByUsername(@RequestParam UUID teamId,
                                                                                       @RequestParam String keyword,
                                                                                       @RequestParam(required = false) String cursor,
                                                                                       @RequestParam(defaultValue = "10") int size){
        return memberService.searchTeamMembersByUsername(teamId, keyword, cursor, size).map(ResponseEntity::ok);
    }

    @PatchMapping
    @Operation(summary = "Update the role of a specific team member.")
    @ApiResponse(responseCode = "200", description = "Update successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid request body.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Not found.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<ActionResponseDto>> updateTeamMemberRole(@RequestParam UUID userId,
                                                                        @Valid @RequestBody UpdateMemberRoleRequestDto request){
        return memberService.updateTeamMemberRole(userId, request).map(ResponseEntity::ok);
    }

    @DeleteMapping
    @Operation(summary = "Remove a member from the team.")
    @ApiResponse(responseCode = "200", description = "Delete successfully.")
    @ApiResponse(responseCode = "404", description = "Not found.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<ActionResponseDto>> removeTeamMember(@RequestParam UUID userId,
                                                                    @Valid @RequestBody RemoveTeamMemberRequestDto request){
        return memberService.removeTeamMember(userId, request).map(ResponseEntity::ok);
    }

    @DeleteMapping("/leave")
    @Operation(summary = "Leave a team.")
    @ApiResponse(responseCode = "200", description = "Leave successfully.")
    @ApiResponse(responseCode = "404", description = "Not found.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<ActionResponseDto>> leaveTeam(@RequestParam UUID userId,
                                                             @RequestParam UUID teamId){
        return memberService.leaveTeam(userId, teamId).map(ResponseEntity::ok);
    }
}
