package com.study.apigateway.mapper;

import com.study.apigateway.dto.Team.response.ActionResponseDto;
import com.study.teamservice.grpc.ActionResponse;

public class ActionMapper {
    private ActionMapper() {}

    public static ActionResponseDto toResponseDto(ActionResponse action) {
        return ActionResponseDto.builder()
                .success(action.getSuccess())
                .message(action.getMessage())
                .build();
    }
}
