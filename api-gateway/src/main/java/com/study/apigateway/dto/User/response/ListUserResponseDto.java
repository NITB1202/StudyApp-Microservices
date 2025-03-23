package com.study.apigateway.dto.User.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListUserResponseDto {
    private List<UserResponseDto> users;

    private Long total;

    private UUID nextCursor;
}
