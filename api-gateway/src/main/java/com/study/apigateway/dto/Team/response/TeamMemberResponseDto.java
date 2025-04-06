package com.study.apigateway.dto.Team.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.study.apigateway.dto.User.response.UserResponseDto;
import com.study.common.enums.TeamRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamMemberResponseDto {
    private TeamRole role;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate joinDate;

    private UserResponseDto user;
}
