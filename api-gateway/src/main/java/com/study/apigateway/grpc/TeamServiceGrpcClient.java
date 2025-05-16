package com.study.apigateway.grpc;

import com.study.apigateway.dto.Notification.request.CreateInvitationRequestDto;
import com.study.apigateway.dto.Team.Team.request.CreateTeamRequestDto;
import com.study.apigateway.dto.Team.Member.request.RemoveTeamMemberRequestDto;
import com.study.apigateway.dto.Team.Member.request.UpdateMemberRoleRequestDto;
import com.study.apigateway.dto.Team.Team.request.UpdateTeamRequestDto;
import com.study.common.grpc.ActionResponse;
import com.study.common.mappers.TeamRoleMapper;
import com.study.teamservice.grpc.*;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
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
                .setDescription(dto.getDescription() != null ? dto.getDescription().trim() : "")
                .build();

        return stub.createTeam(request);
    }

    public TeamDetailResponse getTeamById(UUID id){
        GetTeamByIdRequest request = GetTeamByIdRequest.newBuilder()
                .setId(id.toString())
                .build();

        return stub.getTeamById(request);
    }

    public TeamProfileResponse getTeamByTeamCode(String teamCode) {
        GetTeamByTeamCodeRequest request = GetTeamByTeamCodeRequest.newBuilder()
                .setTeamCode(teamCode)
                .build();

        return stub.getTeamByTeamCode(request);
    }

    public ListTeamResponse getUserTeams(UUID userId, String cursor, int size){
        String handledCursor = cursor == null || cursor.isEmpty() ? "" : cursor;

        GetUserTeamsRequest request = GetUserTeamsRequest.newBuilder()
                .setUserId(userId.toString())
                .setCursor(handledCursor)
                .setSize(size)
                .build();

        return stub.getUserTeams(request);
    }

    public ListTeamResponse searchUserTeamByName(UUID userId, String keyword, String cursor, int size){
        String handledCursor = cursor == null || cursor.isEmpty() ? "" : cursor;

        SearchUserTeamByNameRequest request = SearchUserTeamByNameRequest.newBuilder()
                .setUserId(userId.toString())
                .setKeyword(keyword)
                .setCursor(handledCursor)
                .setSize(size)
                .build();

        return stub.searchUserTeamByName(request);
    }

    public TeamResponse updateTeam(UUID userId, UUID teamId, UpdateTeamRequestDto dto){
        String name = dto.getName() != null ? dto.getName().trim() : "";
        String description = dto.getDescription() != null ? dto.getDescription().trim() : "";

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

    public ActionResponse resetTeamCode(UUID userId, UUID teamId){
        ResetTeamCodeRequest request = ResetTeamCodeRequest.newBuilder()
                .setId(teamId.toString())
                .setUserId(userId.toString())
                .build();

        return stub.resetTeamCode(request);
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

    public TeamMemberResponse getTeamMember(UUID userId, UUID teamId) {
        GetTeamMemberRequest request = GetTeamMemberRequest.newBuilder()
                .setUserId(userId.toString())
                .setTeamId(teamId.toString())
                .build();

        return stub.getTeamMember(request);
    }

    public ListTeamMembersResponse getTeamMembers(UUID teamId, String cursor, int size){
        String handledCursor = cursor == null || cursor.isEmpty() ? "" : cursor;

        GetTeamMembersRequest request = GetTeamMembersRequest.newBuilder()
                .setTeamId(teamId.toString())
                .setCursor(handledCursor)
                .setSize(size)
                .build();

        return stub.getTeamMembers(request);
    }

    public AllTeamMembersResponse getAllTeamMembers(UUID teamId){
        GetAllTeamMembersRequest request = GetAllTeamMembersRequest.newBuilder()
                .setTeamId(teamId.toString())
                .build();

        return stub.getAllTeamMembers(request);
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

    public void validateUpdateTeamResource(UUID userId, UUID teamId) {
        ValidateUpdateTeamResourceRequest request = ValidateUpdateTeamResourceRequest.newBuilder()
                .setUserId(userId.toString())
                .setTeamId(teamId.toString())
                .build();

        stub.validateUpdateTeamResource(request);
    }

    public void validateUsersInTeam(UUID teamId, Set<UUID> userIds) {
        List<String> idsStr = userIds.stream()
                .map(UUID::toString)
                .toList();

        ValidateUsersInTeamRequest request = ValidateUsersInTeamRequest.newBuilder()
                .setTeamId(teamId.toString())
                .addAllUserIds(idsStr)
                .build();

        stub.validateUsersInTeam(request);
    }
}
