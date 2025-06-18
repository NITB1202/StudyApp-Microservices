package com.study.apigateway.dto.Auth.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GenerateAccessTokenResponseDto {
    private String accessToken;
}
