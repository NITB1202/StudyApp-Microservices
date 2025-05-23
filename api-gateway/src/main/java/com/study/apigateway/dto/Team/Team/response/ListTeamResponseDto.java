package com.study.apigateway.dto.Team.Team.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListTeamResponseDto {
    private List<TeamSummaryResponseDto> teams;

    private Long total;

    private String nextCursor;
}
