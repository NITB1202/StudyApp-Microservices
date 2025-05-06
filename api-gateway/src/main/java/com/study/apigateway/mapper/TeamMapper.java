package com.study.apigateway.mapper;

import com.study.apigateway.dto.Team.response.ListTeamResponseDto;
import com.study.apigateway.dto.Team.response.TeamDetailResponseDto;
import com.study.apigateway.dto.Team.response.TeamResponseDto;
import com.study.apigateway.dto.Team.response.TeamSummaryResponseDto;
import com.study.teamservice.grpc.ListTeamResponse;
import com.study.teamservice.grpc.TeamDetailResponse;
import com.study.teamservice.grpc.TeamResponse;
import com.study.teamservice.grpc.TeamSummaryResponse;

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
}
