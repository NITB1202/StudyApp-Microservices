package com.study.apigateway.service.Team;

import com.study.apigateway.dto.Team.request.CreateTeamRequestDto;
import com.study.apigateway.dto.Team.request.UpdateTeamRequestDto;
import com.study.apigateway.dto.Action.ActionResponseDto;
import com.study.apigateway.dto.Team.response.ListTeamResponseDto;
import com.study.apigateway.dto.Team.response.TeamResponseDto;
import com.study.apigateway.grpcclient.TeamServiceGrpcClient;
import com.study.apigateway.mapper.ActionMapper;
import com.study.apigateway.mapper.TeamMapper;
import com.study.common.grpc.ActionResponse;
import com.study.teamservice.grpc.GetFirstTeamIdResponse;
import com.study.teamservice.grpc.ListTeamResponse;
import com.study.teamservice.grpc.TeamResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {
    private final TeamServiceGrpcClient grpcClient;

    @Override
    public Mono<TeamResponseDto> createTeam(CreateTeamRequestDto request) {
        return Mono.fromCallable(() -> {
            TeamResponse team = grpcClient.createTeam(request);
            return TeamMapper.toResponseDto(team);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<TeamResponseDto> getTeamById(UUID id) {
        return Mono.fromCallable(()-> {
            TeamResponse team = grpcClient.getTeamById(id);
            return TeamMapper.toResponseDto(team);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<ListTeamResponseDto> getUserTeams(UUID userId, LocalDate cursor, int size) {
        return Mono.fromCallable(() -> {
            ListTeamResponse response = grpcClient.getUserTeams(userId, cursor, size);

            List<TeamResponseDto> teams = TeamMapper.toResponseDtoList(response.getTeamsList());
            LocalDate nextCursor = response.getNextCursor().isEmpty() ? null : LocalDate.parse(response.getNextCursor());

            return ListTeamResponseDto.builder()
                    .teams(teams)
                    .total(response.getTotal())
                    .nextCursor(nextCursor)
                    .build();
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<ListTeamResponseDto> searchUserTeamByName(UUID userId, String keyword, LocalDate cursor, int size) {
        return Mono.fromCallable(() -> {
            ListTeamResponse response = grpcClient.searchUserTeamByName(userId, keyword, cursor, size);

            List<TeamResponseDto> teams = TeamMapper.toResponseDtoList(response.getTeamsList());
            LocalDate nextCursor = response.getNextCursor().isEmpty() ? null : LocalDate.parse(response.getNextCursor());

            return ListTeamResponseDto.builder()
                    .teams(teams)
                    .total(response.getTotal())
                    .nextCursor(nextCursor)
                    .build();

        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<TeamResponseDto> updateTeam(UUID id, UpdateTeamRequestDto request) {
        return Mono.fromCallable(()->{
            TeamResponse team = grpcClient.updateTeam(id, request);
            return TeamMapper.toResponseDto(team);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<ActionResponseDto> deleteTeam(UUID id) {
        return Mono.fromCallable(()->{
            ActionResponse response = grpcClient.deleteTeam(id);
            return ActionMapper.toResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public UUID getFirstTeamId(UUID userId) {
        GetFirstTeamIdResponse team = grpcClient.getFirstTeamId(userId);
        return UUID.fromString(team.getTeamId());
    }
}
