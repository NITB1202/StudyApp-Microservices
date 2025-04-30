package com.study.teamservice.mapper;

import com.study.common.mappers.TeamRoleMapper;
import com.study.teamservice.entity.TeamUser;
import com.study.teamservice.grpc.ListTeamMembersResponse;
import com.study.teamservice.grpc.TeamMemberResponse;
import com.study.teamservice.grpc.TeamMemberSummaryResponse;

import java.util.List;

public class TeamUserMapper {
    private TeamUserMapper() {}

    public static TeamMemberResponse toTeamMemberResponse(TeamUser teamUser) {
        return TeamMemberResponse.newBuilder()
                .setUserId(teamUser.getUserId().toString())
                .setJoinDate(teamUser.getJoinDate().toString())
                .setRole(TeamRoleMapper.toProtoEnum(teamUser.getRole()))
                .build();
    }

    public static TeamMemberSummaryResponse toTeamMemberSummaryResponse(TeamUser teamUser) {
        return TeamMemberSummaryResponse.newBuilder()
                .setUserId(teamUser.getUserId().toString())
                .setRole(TeamRoleMapper.toProtoEnum(teamUser.getRole()))
                .build();
    }

    public static ListTeamMembersResponse toListTeamMemberResponse(List<TeamUser> teamUsers, long total, int requestSize) {
        List<TeamMemberSummaryResponse> summaries = teamUsers.stream()
                .map(TeamUserMapper::toTeamMemberSummaryResponse)
                .toList();

        String nextCursor = !teamUsers.isEmpty() && teamUsers.size() == requestSize ?
                teamUsers.get(teamUsers.size() - 1).getJoinDate().toString() : "";

        return ListTeamMembersResponse.newBuilder()
                .addAllMembers(summaries)
                .setTotal(total)
                .setNextCursor(nextCursor)
                .build();
    }
}
