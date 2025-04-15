package com.study.apigateway.service.Team;

import com.study.apigateway.dto.Action.ActionResponseDto;
import com.study.apigateway.dto.Notification.request.CreateInvitationRequestDto;
import com.study.apigateway.dto.Team.request.RemoveTeamMemberRequestDto;
import com.study.apigateway.dto.Team.request.UpdateMemberRoleRequestDto;
import com.study.apigateway.dto.Team.response.ListTeamMemberResponseDto;
import com.study.apigateway.dto.Team.response.TeamMemberResponseDto;
import com.study.apigateway.grpc.TeamServiceGrpcClient;
import com.study.apigateway.grpc.UserServiceGrpcClient;
import com.study.apigateway.mapper.ActionMapper;
import com.study.apigateway.mapper.TeamMemberMapper;
import com.study.common.grpc.ActionResponse;
import com.study.teamservice.grpc.GetTeamMembersResponse;
import com.study.teamservice.grpc.TeamMemberResponse;
import com.study.userservice.grpc.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final TeamServiceGrpcClient teamGrpcClient;
    private final UserServiceGrpcClient userGrpcClient;

    @Override
    public Mono<ActionResponseDto> createInvitation(UUID userId, CreateInvitationRequestDto request) {
        return Mono.fromCallable(()->{
            ActionResponse response = teamGrpcClient.createInvitation(userId, request);
            return ActionMapper.toResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<ActionResponseDto> joinTeam(UUID userId, String teamCode) {
        return Mono.fromCallable(()->{
            ActionResponse response = teamGrpcClient.joinTeam(userId, teamCode);
            return ActionMapper.toResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<ListTeamMemberResponseDto> getTeamMembers(UUID teamId, LocalDate cursor, int size) {
        return Mono.fromCallable(()->{
            GetTeamMembersResponse response = teamGrpcClient.getTeamMembers(teamId, cursor, size);

            List<TeamMemberResponse> teamUsers = response.getMembersList();
            LocalDate nextCursor = response.getNextCursor().isEmpty() ? null : LocalDate.parse(response.getNextCursor());

            List<TeamMemberResponseDto> members = new ArrayList<>();

            for(TeamMemberResponse teamUser : teamUsers) {
                UserResponse user = userGrpcClient.getUserById(UUID.fromString(teamUser.getUserId()));
                TeamMemberResponseDto dto = TeamMemberMapper.toTeamMemberResponseDto(teamUser, user);
                members.add(dto);
            }

            return ListTeamMemberResponseDto.builder()
                    .members(members)
                    .total(response.getTotal())
                    .nextCursor(nextCursor)
                    .build();

        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<ActionResponseDto> updateTeamMemberRole(UUID userId, UpdateMemberRoleRequestDto request) {
        return Mono.fromCallable(()->{
            ActionResponse response = teamGrpcClient.updateTeamMemberRole(userId, request);
            return ActionMapper.toResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<ActionResponseDto> removeTeamMember(UUID userId, RemoveTeamMemberRequestDto request) {
        return Mono.fromCallable(()->{
            ActionResponse response = teamGrpcClient.removeTeamMember(userId, request);
            return ActionMapper.toResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<ActionResponseDto> leaveTeam(UUID userId, UUID teamId) {
        return Mono.fromCallable(()->{
            ActionResponse response = teamGrpcClient.leaveTeam(userId, teamId);
            return ActionMapper.toResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
