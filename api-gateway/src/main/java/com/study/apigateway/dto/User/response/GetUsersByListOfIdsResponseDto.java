package com.study.apigateway.dto.User.response;

import com.study.userservice.grpc.UserResponse;
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
public class GetUsersByListOfIdsResponseDto {
    private List<UserResponse> users;

    private Long total;

    private Integer size;

    private UUID nextCursor;
}
