package com.study.apigateway.mapper;

import com.study.apigateway.dto.Action.ActionResponseDto;
import com.study.common.grpc.ActionResponse;

public class ActionMapper {
    private ActionMapper() {}

    public static ActionResponseDto toResponseDto(ActionResponse action) {
        return ActionResponseDto.builder()
                .success(action.getSuccess())
                .message(action.getMessage())
                .build();
    }
}
