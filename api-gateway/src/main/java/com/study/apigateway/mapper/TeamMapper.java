package com.study.apigateway.mapper;

import com.study.apigateway.dto.Team.response.TeamResponseDto;
import com.study.teamservice.grpc.TeamResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class TeamMapper {
    private TeamMapper(){}

    public static TeamResponseDto toResponseDto(TeamResponse response){
        return TeamResponseDto.builder()
                .id(UUID.fromString(response.getId()))
                .name(response.getName())
                .description(response.getDescription())
                .teamCode(response.getTeamCode())
                .createDate(LocalDate.parse(response.getCreateDate()))
                .creatorId(UUID.fromString(response.getCreatorId()))
                .totalMembers(response.getTotalMembers())
                .avatarUrl(response.getAvatarUrl())
                .build();
    }

    public static List<TeamResponseDto> toResponseDtoList(List<TeamResponse> teams){
        return teams.stream().map(TeamMapper::toResponseDto).toList();
    }
}
