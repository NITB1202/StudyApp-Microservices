package com.study.apigateway.dto.Team.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTeamRequestDto {
    @NotNull(message = "User id is required.")
    private UUID userId;

    @Size(min =3, max = 20, message = "Name must be between 3 and 20 characters.")
    private String name;

    private String description;
}
