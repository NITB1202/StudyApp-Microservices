package com.study.apigateway.mapper;

import com.study.apigateway.dto.Team.Team.response.*;
import com.study.teamservice.grpc.*;
import com.study.userservice.grpc.UserDetailResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class TeamMapper {
    private TeamMapper(){}

    public static TeamResponseDto toTeamResponseDto(TeamResponse team){
        return TeamResponseDto.builder()
                .id(UUID.fromString(team.getId()))
                .name(team.getName())
                .description(team.getDescription())
                .build();
    }

    public static TeamDetailResponseDto toTeamDetailResponseDto(TeamDetailResponse team){
        return TeamDetailResponseDto.builder()
                .id(UUID.fromString(team.getId()))
                .name(team.getName())
                .description(team.getDescription())
                .teamCode(team.getTeamCode())
                .createDate(LocalDate.parse(team.getCreateDate()))
                .creatorId(UUID.fromString(team.getCreatorId()))
                .totalMembers(team.getTotalMembers())
                .avatarUrl(team.getAvatarUrl())
                .build();
    }

    public static TeamSummaryResponseDto toTeamSummaryResponseDto(TeamSummaryResponse team){
        return TeamSummaryResponseDto.builder()
                .id(UUID.fromString(team.getId()))
                .name(team.getName())
                .avatarUrl(team.getAvatarUrl())
                .managedByUser(team.getManagedByUser())
                .build();
    }

    public static ListTeamResponseDto toListTeamResponseDto(ListTeamResponse teams){
        List<TeamSummaryResponseDto> summaries = teams.getTeamsList().stream()
                .map(TeamMapper::toTeamSummaryResponseDto)
                .toList();

        return ListTeamResponseDto.builder()
                .teams(summaries)
                .total(teams.getTotal())
                .nextCursor(teams.getNextCursor())
                .build();
    }

    public static TeamProfileResponseDto toTeamProfileResponseDto(TeamProfileResponse team, UserDetailResponse creator){
        return TeamProfileResponseDto.builder()
                .id(UUID.fromString(team.getId()))
                .avatarUrl(team.getAvatarUrl())
                .name(team.getName())
                .description(team.getDescription())
                .createDate(LocalDate.parse(team.getCreateDate()))
                .totalMembers(team.getTotalMembers())
                .creatorName(creator.getUsername())
                .creatorAvatarUrl(creator.getAvatarUrl())
                .build();
    }
}
