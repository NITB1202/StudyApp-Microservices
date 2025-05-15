package com.study.apigateway.service.Team;

import com.study.apigateway.dto.Team.Team.request.CreateTeamRequestDto;
import com.study.apigateway.dto.Team.Team.request.UpdateTeamRequestDto;
import com.study.apigateway.dto.Action.ActionResponseDto;
import com.study.apigateway.dto.Team.Team.response.ListTeamResponseDto;
import com.study.apigateway.dto.Team.Team.response.TeamDetailResponseDto;
import com.study.apigateway.dto.Team.Team.response.TeamProfileResponseDto;
import com.study.apigateway.dto.Team.Team.response.TeamResponseDto;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface TeamService {
    Mono<TeamResponseDto> createTeam(UUID userId, CreateTeamRequestDto request);
    Mono<TeamDetailResponseDto> getTeamById(UUID id);
    Mono<TeamProfileResponseDto> getTeamByTeamCode(String teamCode);
    Mono<ListTeamResponseDto> getUserTeams(UUID userId, String cursor, int size);
    Mono<ListTeamResponseDto> searchUserTeamByName(UUID userId, String keyword, String cursor, int size);
    Mono<TeamResponseDto> updateTeam(UUID userId, UUID teamId, UpdateTeamRequestDto request);
    Mono<ActionResponseDto> resetTeamCode(UUID userId, UUID teamId);
    Mono<ActionResponseDto> deleteTeam(UUID id, UUID userId);
}
