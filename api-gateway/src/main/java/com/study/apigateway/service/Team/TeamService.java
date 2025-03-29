package com.study.apigateway.service.Team;

import com.study.apigateway.dto.Team.request.CreateTeamRequestDto;
import com.study.apigateway.dto.Team.request.UpdateTeamRequestDto;
import com.study.apigateway.dto.Team.response.ActionResponseDto;
import com.study.apigateway.dto.Team.response.ListTeamResponseDto;
import com.study.apigateway.dto.Team.response.TeamResponseDto;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.UUID;

public interface TeamService {
    Mono<TeamResponseDto> createTeam(CreateTeamRequestDto request);
    Mono<TeamResponseDto> getTeamById(UUID id);
    Mono<ListTeamResponseDto> getUserTeams(UUID userId, LocalDate cursor, int size);
    Mono<ListTeamResponseDto> searchUserTeamByName(UUID userId, String keyword, LocalDate cursor, int size);
    Mono<TeamResponseDto> updateTeam(UUID id, UpdateTeamRequestDto request, FilePart newAvatar);
    Mono<ActionResponseDto> deleteTeam(UUID id);

    UUID getFirstTeamId(UUID userId);
}
