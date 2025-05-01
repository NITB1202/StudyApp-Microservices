package com.study.apigateway.dto.Team.response;

import com.study.common.enums.TeamRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamMemberResponseDto {
    private UUID userId;

    private String username;

    private String avatarUrl;

    private TeamRole role;
}
