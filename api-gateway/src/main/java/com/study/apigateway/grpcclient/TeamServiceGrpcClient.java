package com.study.apigateway.grpcclient;

import com.study.apigateway.dto.Team.request.CreateTeamRequestDto;
import com.study.apigateway.dto.Team.request.UpdateTeamRequestDto;
import com.study.common.grpc.ActionResponse;
import com.study.teamservice.grpc.*;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class TeamServiceGrpcClient {
    @GrpcClient("team-service")
    private TeamServiceGrpc.TeamServiceBlockingStub stub;

    public TeamResponse createTeam(CreateTeamRequestDto dto){
        CreateTeamRequest request = CreateTeamRequest.newBuilder()
                .setCreatorId(dto.getCreatorId().toString())
                .setName(dto.getName())
                .setDescription(dto.getDescription() != null ? dto.getDescription() : "")
                .build();

        return stub.createTeam(request);
    }

    public TeamResponse getTeamById(UUID id){
        GetTeamByIdRequest request = GetTeamByIdRequest.newBuilder()
                .setId(id.toString())
                .build();

        return stub.getTeamById(request);
    }

    public GetFirstTeamIdResponse getFirstTeamId(UUID userId){
        GetFirstTeamIdRequest request = GetFirstTeamIdRequest.newBuilder()
                .setUserId(userId.toString())
                .build();

        return stub.getFirstTeamId(request);
    }

    public ListTeamResponse getUserTeams(UUID userId, LocalDate cursor, int size){
        String cursorDate = cursor != null ? cursor.toString() : "";

        GetUserTeamsRequest request = GetUserTeamsRequest.newBuilder()
                .setUserId(userId.toString())
                .setCursor(cursorDate)
                .setSize(size)
                .build();

        return stub.getUserTeams(request);
    }

    public ListTeamResponse searchUserTeamByName(UUID userId, String keyword, LocalDate cursor, int size){
        String dateCursor = cursor != null ? cursor.toString() : "";

        SearchUserTeamByNameRequest request = SearchUserTeamByNameRequest.newBuilder()
                .setUserId(userId.toString())
                .setKeyword(keyword)
                .setCursor(dateCursor)
                .setSize(size)
                .build();

        return stub.searchUserTeamByName(request);
    }

    public TeamResponse updateTeam(UUID id, UpdateTeamRequestDto dto){
        String name = dto.getName() != null ? dto.getName() : "";
        String description = dto.getDescription() != null ? dto.getDescription() : "";

        UpdateTeamRequest request = UpdateTeamRequest.newBuilder()
                .setId(id.toString())
                .setUserId(dto.getUserId().toString())
                .setName(name)
                .setDescription(description)
                .build();

        return stub.updateTeam(request);
    }

    public ActionResponse uploadTeamAvatar(UUID teamId, UUID userId, String avatarUrl){
        UploadTeamAvatarRequest request = UploadTeamAvatarRequest.newBuilder()
                .setTeamId(teamId.toString())
                .setUserId(userId.toString())
                .setAvatarUrl(avatarUrl)
                .build();

        return stub.uploadTeamAvatar(request);
    }

    public ActionResponse deleteTeam(UUID id, UUID userId){
        DeleteTeamRequest request = DeleteTeamRequest.newBuilder()
                .setId(id.toString())
                .setUserId(userId.toString())
                .build();

        return stub.deleteTeam(request);
    }
}
