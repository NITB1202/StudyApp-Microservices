package com.study.apigateway.dto.Team.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.study.common.enums.TeamRole;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMemberRoleRequestDto {
    @NotNull(message = "Team id is required")
    private UUID teamId;

    @NotNull(message = "Member id is required")
    private UUID memberId;

    @NotNull(message = "Role is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private TeamRole role;
}
