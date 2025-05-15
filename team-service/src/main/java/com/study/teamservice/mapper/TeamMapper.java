package com.study.teamservice.mapper;

import com.study.teamservice.entity.Team;
import com.study.teamservice.grpc.*;

import java.util.List;

public class TeamMapper {
    private TeamMapper() {}

    public static TeamResponse toTeamResponse(Team team) {
        return TeamResponse.newBuilder()
                .setId(team.getId().toString())
                .setName(team.getName())
                .setDescription(team.getDescription())
                .build();
    }

    public static TeamDetailResponse toTeamDetailResponse(Team team) {
        return TeamDetailResponse.newBuilder()
                .setId(team.getId().toString())
                .setName(team.getName())
                .setTeamCode(team.getTeamCode())
                .setCreateDate(team.getCreateDate().toString())
                .setCreatorId(team.getCreatorId().toString())
                .setTotalMembers(team.getTotalMembers())
                .setDescription(team.getDescription())
                .setAvatarUrl(team.getAvatarUrl())
                .build();
    }

    public static TeamSummaryResponse toTeamSummaryResponse(Team team, boolean managedByUser) {
        return TeamSummaryResponse.newBuilder()
                .setId(team.getId().toString())
                .setName(team.getName())
                .setAvatarUrl(team.getAvatarUrl())
                .setManagedByUser(managedByUser)
                .build();
    }

    public static ListTeamResponse toListTeamResponse(List<TeamSummaryResponse> teams, long total,  String nextCursor) {
        return ListTeamResponse.newBuilder()
                .addAllTeams(teams)
                .setTotal(total)
                .setNextCursor(nextCursor)
                .build();
    }

    public static TeamProfileResponse toTeamProfileResponse(Team team) {
        return TeamProfileResponse.newBuilder()
                .setId(team.getId().toString())
                .setName(team.getName())
                .setAvatarUrl(team.getAvatarUrl())
                .setDescription(team.getDescription())
                .setCreatorId(team.getCreatorId().toString())
                .setCreateDate(team.getCreateDate().toString())
                .setTotalMembers(team.getTotalMembers())
                .build();
    }
}