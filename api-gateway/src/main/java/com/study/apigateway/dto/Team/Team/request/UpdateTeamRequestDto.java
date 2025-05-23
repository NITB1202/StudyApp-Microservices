package com.study.apigateway.dto.Team.Team.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTeamRequestDto {
    @Size(min =3, max = 20, message = "Name must be between 3 and 20 characters.")
    private String name;

    private String description;
}
