package com.study.apigateway.dto.Team.Team.response;

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
public class TeamProfileResponseDto {
    private UUID id;

    private String avatarUrl;

    private String name;

    private String description;

    private LocalDate createDate;

    private Long totalMembers;

    private String creatorName;

    private String creatorAvatarUrl;
}
