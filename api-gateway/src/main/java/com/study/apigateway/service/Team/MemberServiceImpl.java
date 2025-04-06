package com.study.apigateway.service.Team;

import com.study.apigateway.dto.Action.ActionResponseDto;
import com.study.apigateway.dto.Notification.request.CreateInvitationRequestDto;
import com.study.apigateway.dto.Team.request.RemoveTeamMemberRequestDto;
import com.study.apigateway.dto.Team.request.UpdateMemberRoleRequestDto;
import com.study.apigateway.dto.Team.response.ListTeamMemberResponseDto;
import com.study.apigateway.dto.Team.response.TeamMemberResponseDto;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.UUID;

public class MemberServiceImpl implements MemberService {
    @Override
    public Mono<ActionResponseDto> createInvitation(UUID userId, CreateInvitationRequestDto request) {
        return null;
    }

    @Override
    public Mono<ActionResponseDto> joinTeam(UUID userId, String teamCode) {
        return null;
    }

    @Override
    public Mono<TeamMemberResponseDto> getTeamMemberById(UUID teamId, UUID memberId) {
        return null;
    }

    @Override
    public Mono<ListTeamMemberResponseDto> getTeamMembers(UUID teamId, LocalDate cursor, int size) {
        return null;
    }

    @Override
    public Mono<ActionResponseDto> updateTeamMemberRole(UUID userId, UpdateMemberRoleRequestDto request) {
        return null;
    }

    @Override
    public Mono<ActionResponseDto> removeTeamMember(UUID userId, RemoveTeamMemberRequestDto request) {
        return null;
    }

    @Override
    public Mono<ActionResponseDto> leaveTeam(UUID userId, UUID teamId) {
        return null;
    }
}
