package com.study.authservice.mapper;

import com.study.authservice.grpc.IsAccountRegisteredResponse;
import com.study.authservice.grpc.LoginResponse;

public class AuthMapper {
    private AuthMapper() {}

    public static LoginResponse toLoginResponse(String accessToken, String refreshToken) {
        return LoginResponse.newBuilder()
                .setAccessToken(accessToken)
                .setRefreshToken(refreshToken)
                .build();
    }

    public static IsAccountRegisteredResponse toIsAccountRegisteredResponse(boolean isRegistered) {
        return IsAccountRegisteredResponse.newBuilder()
                .setIsRegistered(isRegistered)
                .build();
    }
}
