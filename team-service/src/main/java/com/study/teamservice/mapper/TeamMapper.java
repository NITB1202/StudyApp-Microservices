package com.study.teamservice.mapper;

import com.study.teamservice.entity.Team;
import com.study.teamservice.grpc.TeamResponse;

import java.util.List;
import java.util.Objects;

public class TeamMapper {
    private TeamMapper() {}

    public static TeamResponse toTeamResponse(Team team) {
        return TeamResponse.newBuilder()
                .setId(team.getId().toString())
                .setName(team.getName())
                .setDescription(team.getDescription())
                .setTeamCode(team.getTeamCode())
                .setCreateDate(team.getCreateDate().toString())
                .setCreatorId(team.getCreatorId().toString())
                .setTotalMembers(team.getTotalMembers())
                .setAvatarUrl(Objects.requireNonNullElse(team.getAvatarUrl(), ""))
                .build();
    }

    public static List<TeamResponse> toTeamResponseList(List<Team> teams) {
        return teams.stream()
                .map(TeamMapper::toTeamResponse)
                .toList();
    }
}