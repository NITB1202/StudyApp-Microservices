package com.study.apigateway.service.Team;

import com.study.apigateway.dto.Action.ActionResponseDto;
import com.study.apigateway.dto.Notification.request.CreateInvitationRequestDto;
import com.study.apigateway.dto.Team.request.RemoveTeamMemberRequestDto;
import com.study.apigateway.dto.Team.request.UpdateMemberRoleRequestDto;
import com.study.apigateway.dto.Team.response.ListTeamMemberResponseDto;
import com.study.apigateway.dto.Team.response.TeamUserProfileResponseDto;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.UUID;

public interface MemberService {
    Mono<ActionResponseDto> createInvitation(UUID userId, CreateInvitationRequestDto request);
    Mono<ActionResponseDto> joinTeam(UUID userId, String teamCode);
    Mono<TeamUserProfileResponseDto> getUserInTeam(UUID userId, UUID teamId);
    Mono<ListTeamMemberResponseDto> getTeamMembers(UUID teamId, LocalDate cursor, int size);
    Mono<ListTeamMemberResponseDto> searchTeamMembersByUsername(UUID teamId, String keyword, LocalDate cursor, int size);
    Mono<ActionResponseDto> updateTeamMemberRole(UUID userId, UpdateMemberRoleRequestDto request);
    Mono<ActionResponseDto> removeTeamMember(UUID userId, RemoveTeamMemberRequestDto request);
    Mono<ActionResponseDto> leaveTeam(UUID userId, UUID teamId);
}
