package com.study.apigateway.dto.Team.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeamDetailResponseDto {
    private UUID id;

    private String name;

    private String description;

    private String teamCode;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate createDate;

    private UUID creatorId;

    private Long totalMembers;

    private String avatarUrl;
}
