package com.study.apigateway.service.Auth;


import com.study.apigateway.dto.Action.ActionResponseDto;
import com.study.apigateway.dto.Auth.request.*;
import com.study.apigateway.dto.Auth.response.GenerateAccessTokenResponseDto;
import com.study.apigateway.dto.Auth.response.LoginResponseDto;
import reactor.core.publisher.Mono;

public interface AuthService {
    Mono<LoginResponseDto> loginWithCredentials(LoginWithCredentialsRequestDto request);
    Mono<LoginResponseDto> loginWithProvider(LoginWithProviderRequestDto request);
    Mono<ActionResponseDto> validateRegisterInfo(ValidateRegisterInfoRequestDto request);
    Mono<ActionResponseDto> registerWithCredentials(RegisterWithCredentialsRequestDto request);
    Mono<ActionResponseDto> sendResetPasswordCode(String email);
    Mono<ActionResponseDto> resetPassword(ResetPasswordRequestDto request);
    Mono<GenerateAccessTokenResponseDto> generateAccessToken(String refreshToken);
}
