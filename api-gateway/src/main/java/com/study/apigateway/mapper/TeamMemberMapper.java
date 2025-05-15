package com.study.apigateway.mapper;

import com.study.apigateway.dto.Team.Member.response.ListTeamMemberResponseDto;
import com.study.apigateway.dto.Team.Member.response.TeamMemberResponseDto;
import com.study.apigateway.dto.Team.Member.response.TeamMemberProfileResponseDto;
import com.study.common.mappers.TeamRoleMapper;
import com.study.teamservice.grpc.TeamMemberResponse;
import com.study.teamservice.grpc.TeamMemberSummaryResponse;
import com.study.userservice.grpc.UserDetailResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class TeamMemberMapper {
    private TeamMemberMapper(){}

    public static TeamMemberResponseDto toTeamMemberResponseDto(TeamMemberSummaryResponse teamUser,
                                                                UserDetailResponse user) {
        return TeamMemberResponseDto.builder()
                .userId(UUID.fromString(user.getId()))
                .username(user.getUsername())
                .avatarUrl(user.getAvatarUrl())
                .role(TeamRoleMapper.toEnum(teamUser.getRole()))
                .build();
    }

    public static TeamMemberResponseDto toTeamMemberResponseDto(TeamMemberResponse teamUser,
                                                                UserDetailResponse user) {
        return TeamMemberResponseDto.builder()
                .userId(UUID.fromString(user.getId()))
                .username(user.getUsername())
                .avatarUrl(user.getAvatarUrl())
                .role(TeamRoleMapper.toEnum(teamUser.getRole()))
                .build();
    }

    public static ListTeamMemberResponseDto toListTeamMemberResponseDto(List<TeamMemberResponseDto> members,
                                                                        long total,
                                                                        String nextCursor) {
        return ListTeamMemberResponseDto.builder()
                .members(members)
                .total(total)
                .nextCursor(nextCursor)
                .build();
    }

    public static TeamMemberProfileResponseDto toTeamUserProfileResponseDto(TeamMemberResponse member) {
        return TeamMemberProfileResponseDto.builder()
                .userId(UUID.fromString(member.getUserId()))
                .role(TeamRoleMapper.toEnum(member.getRole()))
                .joinDate(LocalDate.parse(member.getJoinDate()))
                .build();
    }
}
