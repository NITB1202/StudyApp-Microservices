package com.study.apigateway.service.Team;

import com.study.apigateway.dto.Team.request.CreateTeamRequestDto;
import com.study.apigateway.dto.Team.request.UpdateTeamRequestDto;
import com.study.apigateway.dto.Action.ActionResponseDto;
import com.study.apigateway.dto.Team.response.ListTeamResponseDto;
import com.study.apigateway.dto.Team.response.TeamResponseDto;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.UUID;

public interface TeamService {
    Mono<TeamResponseDto> createTeam(UUID userId, CreateTeamRequestDto request);
    Mono<TeamResponseDto> getTeamById(UUID id);
    Mono<ListTeamResponseDto> getUserTeams(UUID userId, LocalDate cursor, int size);
    Mono<ListTeamResponseDto> searchUserTeamByName(UUID userId, String keyword, LocalDate cursor, int size);
    Mono<TeamResponseDto> updateTeam(UUID id, UpdateTeamRequestDto request);
    Mono<ActionResponseDto> deleteTeam(UUID id, UUID userId);

    UUID getFirstTeamId(UUID userId);
}
