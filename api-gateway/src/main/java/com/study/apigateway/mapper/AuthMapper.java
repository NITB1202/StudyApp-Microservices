package com.study.apigateway.mapper;

import com.study.apigateway.dto.Auth.response.GenerateAccessTokenResponseDto;
import com.study.apigateway.dto.Auth.response.LoginResponseDto;
import com.study.authservice.grpc.LoginResponse;

public class AuthMapper {
    private AuthMapper() {}

    public static LoginResponseDto toLoginResponseDto(LoginResponse login) {
        return LoginResponseDto.builder()
                .accessToken(login.getAccessToken())
                .refreshToken(login.getRefreshToken())
                .build();
    }

    public static GenerateAccessTokenResponseDto toGenerateAccessTokenResponseDto(String accessToken) {
        return GenerateAccessTokenResponseDto.builder()
                .accessToken(accessToken)
                .build();
    }
}
