package com.study.teamservice.mapper;

import com.study.common.mappers.TeamRoleMapper;
import com.study.teamservice.entity.TeamUser;
import com.study.teamservice.grpc.TeamMemberResponse;

public class TeamUserMapper {
    private TeamUserMapper() {}

    public static TeamMemberResponse toTeamMemberResponse(TeamUser teamUser) {
        return TeamMemberResponse.newBuilder()
                .setUserId(teamUser.getUserId().toString())
                .setJoinDate(teamUser.getJoinDate().toString())
                .setRole(TeamRoleMapper.toProtoEnum(teamUser.getRole()))
                .build();
    }
}
