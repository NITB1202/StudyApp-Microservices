package com.study.apigateway.dto.Team.Member.response;

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
