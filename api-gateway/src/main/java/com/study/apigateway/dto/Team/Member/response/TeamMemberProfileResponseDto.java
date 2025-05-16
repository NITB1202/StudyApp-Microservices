package com.study.apigateway.dto.Team.Member.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.study.common.enums.TeamRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamMemberProfileResponseDto {
    private UUID userId;

    private TeamRole role;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate joinDate;
}
