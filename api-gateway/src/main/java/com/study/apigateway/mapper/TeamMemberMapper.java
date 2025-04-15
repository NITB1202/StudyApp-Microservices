package com.study.apigateway.mapper;

import com.study.apigateway.dto.Team.response.TeamMemberResponseDto;
import com.study.common.mappers.TeamRoleMapper;
import com.study.teamservice.grpc.TeamMemberResponse;
import com.study.userservice.grpc.UserResponse;

import java.time.LocalDate;
import java.util.UUID;

public class TeamMemberMapper {
    private TeamMemberMapper(){}

    public static TeamMemberResponseDto toTeamMemberResponseDto(TeamMemberResponse teamUser, UserResponse user) {
        return TeamMemberResponseDto.builder()
                .userId(UUID.fromString(user.getId()))
                .username(user.getUsername())
                .avatarUrl(user.getAvatarUrl())
                .role(TeamRoleMapper.toEnum(teamUser.getRole()))
                .joinDate(LocalDate.parse(teamUser.getJoinDate()))
                .build();
    }
}
