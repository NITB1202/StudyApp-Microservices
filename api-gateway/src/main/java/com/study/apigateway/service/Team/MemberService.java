package com.study.apigateway.service.Team;

import com.study.apigateway.dto.Action.ActionResponseDto;
import com.study.apigateway.dto.Notification.request.CreateInvitationRequestDto;
import com.study.apigateway.dto.Team.Member.request.RemoveTeamMemberRequestDto;
import com.study.apigateway.dto.Team.Member.request.UpdateMemberRoleRequestDto;
import com.study.apigateway.dto.Team.Member.response.ListTeamMemberResponseDto;
import com.study.apigateway.dto.Team.Member.response.TeamMemberProfileResponseDto;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface MemberService {
    Mono<ActionResponseDto> createInvitation(UUID userId, CreateInvitationRequestDto request);
    Mono<ActionResponseDto> joinTeam(UUID userId, String teamCode);
    Mono<TeamMemberProfileResponseDto> getUserInTeam(UUID userId, UUID teamId);
    Mono<ListTeamMemberResponseDto> getTeamMembers(UUID teamId, String cursor, int size);
    Mono<ListTeamMemberResponseDto> searchTeamMembersByUsername(UUID teamId, String keyword, String cursor, int size);
    Mono<ActionResponseDto> updateTeamMemberRole(UUID userId, UpdateMemberRoleRequestDto request);
    Mono<ActionResponseDto> removeTeamMember(UUID userId, RemoveTeamMemberRequestDto request);
    Mono<ActionResponseDto> leaveTeam(UUID userId, UUID teamId);
}
