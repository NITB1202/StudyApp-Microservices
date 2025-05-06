package com.study.apigateway.dto.Team.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListTeamMemberResponseDto {
    private List<TeamMemberResponseDto> members;

    private Long total;

    private String nextCursor;
}
