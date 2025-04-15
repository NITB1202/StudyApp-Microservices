package com.study.apigateway.grpc;

import com.study.apigateway.dto.Notification.request.CreateInvitationRequestDto;
import com.study.apigateway.dto.Team.request.CreateTeamRequestDto;
import com.study.apigateway.dto.Team.request.RemoveTeamMemberRequestDto;
import com.study.apigateway.dto.Team.request.UpdateMemberRoleRequestDto;
import com.study.apigateway.dto.Team.request.UpdateTeamRequestDto;
import com.study.common.grpc.ActionResponse;
import com.study.common.mappers.TeamRoleMapper;
import com.study.teamservice.grpc.*;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class TeamServiceGrpcClient {
    @GrpcClient("team-service")
    private TeamServiceGrpc.TeamServiceBlockingStub stub;

    //Team section
    public TeamResponse createTeam(UUID userId, CreateTeamRequestDto dto){
        CreateTeamRequest request = CreateTeamRequest.newBuilder()
                .setCreatorId(userId.toString())
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

    public TeamResponse updateTeam(UUID userId, UUID teamId, UpdateTeamRequestDto dto){
        String name = dto.getName() != null ? dto.getName() : "";
        String description = dto.getDescription() != null ? dto.getDescription() : "";

        UpdateTeamRequest request = UpdateTeamRequest.newBuilder()
                .setId(teamId.toString())
                .setUserId(userId.toString())
                .setName(name)
                .setDescription(description)
                .build();

        return stub.updateTeam(request);
    }

    public ActionResponse uploadTeamAvatar(UUID userId, UUID teamId, String avatarUrl){
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

    //Member section
    public ActionResponse createInvitation(UUID userId, CreateInvitationRequestDto dto){
        CreateInvitationRequest request = CreateInvitationRequest.newBuilder()
                .setTeamId(dto.getTeamId().toString())
                .setInviterId(userId.toString())
                .setInviteeId(dto.getInviteeId().toString())
                .build();

        return stub.createInvitation(request);
    }

    public ActionResponse joinTeam(UUID userId, String teamCode){
        JoinTeamRequest request = JoinTeamRequest.newBuilder()
                .setUserId(userId.toString())
                .setTeamCode(teamCode)
                .build();

        return stub.joinTeam(request);
    }

    public GetTeamMembersResponse getTeamMembers(UUID teamId, LocalDate cursor, int size){
        String cursorDate = cursor != null ? cursor.toString() : "";

        GetTeamMembersRequest request = GetTeamMembersRequest.newBuilder()
                .setTeamId(teamId.toString())
                .setCursor(cursorDate)
                .setSize(size)
                .build();

        return stub.getTeamMembers(request);
    }

    public ActionResponse updateTeamMemberRole(UUID userId, UpdateMemberRoleRequestDto dto){
        UpdateMemberRoleRequest request = UpdateMemberRoleRequest.newBuilder()
                .setUserId(userId.toString())
                .setTeamId(dto.getTeamId().toString())
                .setMemberId(dto.getMemberId().toString())
                .setRole(TeamRoleMapper.toProtoEnum(dto.getRole()))
                .build();

        return stub.updateTeamMemberRole(request);
    }

    public ActionResponse removeTeamMember(UUID userId, RemoveTeamMemberRequestDto dto){
        RemoveTeamMemberRequest request = RemoveTeamMemberRequest.newBuilder()
                .setUserId(userId.toString())
                .setTeamId(dto.getTeamId().toString())
                .setMemberId(dto.getMemberId().toString())
                .build();

        return stub.removeTeamMember(request);
    }

    public ActionResponse leaveTeam(UUID userId, UUID teamId){
        LeaveTeamRequest request = LeaveTeamRequest.newBuilder()
                .setUserId(userId.toString())
                .setTeamId(teamId.toString())
                .build();

        return stub.leaveTeam(request);
    }
}
