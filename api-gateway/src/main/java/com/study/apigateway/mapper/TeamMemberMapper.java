package com.study.apigateway.mapper;

import com.study.apigateway.dto.Team.response.TeamMemberResponseDto;
import com.study.common.mappers.TeamRoleMapper;
import com.study.teamservice.grpc.TeamMemberResponse;
import com.study.userservice.grpc.UserResponse;

import java.time.LocalDate;

public class TeamMemberMapper {
    private TeamMemberMapper(){}

    public static TeamMemberResponseDto toTeamMemberResponseDto(TeamMemberResponse teamUser, UserResponse user) {
        return TeamMemberResponseDto.builder()
                .role(TeamRoleMapper.toEnum(teamUser.getRole()))
                .joinDate(LocalDate.parse(teamUser.getJoinDate()))
                .user(UserMapper.toResponseDto(user))
                .build();
    }
}
