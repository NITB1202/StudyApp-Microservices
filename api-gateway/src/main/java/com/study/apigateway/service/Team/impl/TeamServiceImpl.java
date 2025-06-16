package com.study.apigateway.service.Team.impl;

import com.study.apigateway.dto.Team.Team.request.CreateTeamRequestDto;
import com.study.apigateway.dto.Team.Team.request.UpdateTeamRequestDto;
import com.study.apigateway.dto.Action.ActionResponseDto;
import com.study.apigateway.dto.Team.Team.response.ListTeamResponseDto;
import com.study.apigateway.dto.Team.Team.response.TeamDetailResponseDto;
import com.study.apigateway.dto.Team.Team.response.TeamProfileResponseDto;
import com.study.apigateway.dto.Team.Team.response.TeamResponseDto;
import com.study.apigateway.grpc.DocumentServiceGrpcClient;
import com.study.apigateway.grpc.TeamServiceGrpcClient;
import com.study.apigateway.grpc.UserServiceGrpcClient;
import com.study.apigateway.mapper.ActionMapper;
import com.study.apigateway.mapper.TeamMapper;
import com.study.apigateway.service.Team.TeamService;
import com.study.apigateway.util.FileUtils;
import com.study.common.exceptions.BusinessException;
import com.study.common.grpc.ActionResponse;
import com.study.documentservice.grpc.UploadImageResponse;
import com.study.teamservice.grpc.ListTeamResponse;
import com.study.teamservice.grpc.TeamDetailResponse;
import com.study.teamservice.grpc.TeamProfileResponse;
import com.study.teamservice.grpc.TeamResponse;
import com.study.userservice.grpc.UserDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {
    private final TeamServiceGrpcClient grpcClient;
    private final UserServiceGrpcClient userClient;
    private final DocumentServiceGrpcClient documentClient;
    private final String AVATAR_FOLDER = "teams";

    @Override
    public Mono<TeamResponseDto> createTeam(UUID userId, CreateTeamRequestDto request) {
        return Mono.fromCallable(() -> {
            TeamResponse team = grpcClient.createTeam(userId, request);
            return TeamMapper.toTeamResponseDto(team);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<TeamDetailResponseDto> getTeamById(UUID id) {
        return Mono.fromCallable(()-> {
            TeamDetailResponse team = grpcClient.getTeamById(id);
            return TeamMapper.toTeamDetailResponseDto(team);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<TeamProfileResponseDto> getTeamByTeamCode(String teamCode) {
        return Mono.fromCallable(()->{
            TeamProfileResponse team = grpcClient.getTeamByTeamCode(teamCode);

            UUID creatorId = UUID.fromString(team.getCreatorId());
            UserDetailResponse creator = userClient.getUserById(creatorId);

            return TeamMapper.toTeamProfileResponseDto(team, creator);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<ListTeamResponseDto> getUserTeams(UUID userId, String cursor, int size) {
        return Mono.fromCallable(() -> {
            ListTeamResponse teams = grpcClient.getUserTeams(userId, cursor, size);
            return TeamMapper.toListTeamResponseDto(teams);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<ListTeamResponseDto> searchUserTeamByName(UUID userId, String keyword, String cursor, int size) {
        return Mono.fromCallable(() -> {
            ListTeamResponse teams = grpcClient.searchUserTeamByName(userId, keyword, cursor, size);
            return TeamMapper.toListTeamResponseDto(teams);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<TeamResponseDto> updateTeam(UUID userId, UUID teamId, UpdateTeamRequestDto request) {
        return Mono.fromCallable(()->{
            TeamResponse team = grpcClient.updateTeam(userId, teamId, request);
            return TeamMapper.toTeamResponseDto(team);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<ActionResponseDto> resetTeamCode(UUID userId, UUID teamId) {
        return Mono.fromCallable(()->{
            ActionResponse response = grpcClient.resetTeamCode(userId, teamId);
            return ActionMapper.toResponseDto(response);
        });
    }

    @Override
    public Mono<ActionResponseDto> deleteTeam(UUID id, UUID userId) {
        return Mono.fromCallable(()->{
            ActionResponse response = grpcClient.deleteTeam(id, userId);
            return ActionMapper.toResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<ActionResponseDto> uploadTeamAvatar(UUID userId, UUID id, FilePart file) {
        if(!FileUtils.isImage(file)) {
            throw new BusinessException("Team's avatar must be an image.");
        }

        return DataBufferUtils.join(file.content())
                .flatMap(buffer -> {
                    byte[] bytes = new byte[buffer.readableByteCount()];
                    buffer.read(bytes);
                    DataBufferUtils.release(buffer);

                    UploadImageResponse avatar = documentClient.uploadImage(AVATAR_FOLDER, id.toString(), bytes);
                    ActionResponse response = grpcClient.uploadTeamAvatar(userId, id, avatar.getUrl());

                    return Mono.fromCallable(() -> ActionMapper.toResponseDto(response))
                            .subscribeOn(Schedulers.boundedElastic());
                });
    }
}
