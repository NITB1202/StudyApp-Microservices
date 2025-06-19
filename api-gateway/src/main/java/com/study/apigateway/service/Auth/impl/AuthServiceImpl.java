package com.study.apigateway.service.Auth.impl;


import com.study.apigateway.dto.Action.ActionResponseDto;
import com.study.apigateway.dto.Auth.request.*;
import com.study.apigateway.dto.Auth.response.GenerateAccessTokenResponseDto;
import com.study.apigateway.dto.Auth.response.LoginResponseDto;
import com.study.apigateway.dto.Auth.response.OAuthUserInfo;
import com.study.apigateway.grpc.AuthServiceGrpcClient;
import com.study.apigateway.grpc.UserServiceGrpcClient;
import com.study.apigateway.mapper.ActionMapper;
import com.study.apigateway.mapper.AuthMapper;
import com.study.apigateway.service.Auth.AuthService;
import com.study.authservice.grpc.AccountSummaryResponse;
import com.study.authservice.grpc.LoginResponse;
import com.study.authservice.grpc.VerificationType;
import com.study.common.enums.Provider;
import com.study.common.enums.UserRole;
import com.study.common.exceptions.BusinessException;
import com.study.common.grpc.ActionResponse;
import com.study.common.mappers.UserRoleMapper;
import com.study.common.utils.JwtUtils;
import com.study.userservice.grpc.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthServiceGrpcClient authGrpc;
    private final UserServiceGrpcClient userGrpc;
    private final WebClient webClient;

    @Override
    public Mono<LoginResponseDto> loginWithCredentials(LoginWithCredentialsRequestDto request) {
        return Mono.fromCallable(()->{
            LoginResponse response = authGrpc.loginWithCredentials(request);
            return AuthMapper.toLoginResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<LoginResponseDto> loginWithProvider(LoginWithProviderRequestDto request) {
        if(request.getProvider() == Provider.LOCAL) {
            throw new BusinessException("Please use the standard login with provider LOCAL.");
        }

        Mono<OAuthUserInfo> userInfoMono = null;

        switch (request.getProvider()) {
            case GOOGLE -> userInfoMono = getUserInfoWithGoogle(request.getAccessToken());
        }

        return userInfoMono.flatMap(userInfo ->{
            boolean isRegistered = authGrpc.isAccountRegistered(userInfo.getId()).getIsRegistered();

            if(!isRegistered) {
                UserResponse user = userGrpc.createUser(userInfo.getName(), null, null, userInfo.getPicture());
                UUID userId = UUID.fromString(user.getId());
                authGrpc.registerWithProvider(userInfo.getId(), request.getProvider(), userId, userInfo.getEmail());
            }

            LoginResponse response = authGrpc.loginWithProvider(userInfo.getId(), request.getProvider());
            return Mono.just(AuthMapper.toLoginResponseDto(response));
        });
    }

    @Override
    public Mono<ActionResponseDto> validateRegisterInfo(ValidateRegisterInfoRequestDto request) {
        return Mono.fromCallable(()->{
            ActionResponse response = authGrpc.validateRegisterInfo(request);

            if(response.getSuccess()) {
                authGrpc.sendVerificationEmail(request.getEmail(), VerificationType.REGISTER);
            }

            return ActionMapper.toResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<ActionResponseDto> registerWithCredentials(RegisterWithCredentialsRequestDto request) {
        return Mono.fromCallable(()->{
            UserResponse user = userGrpc.createUser(request.getUsername(), request.getDateOfBirth().toString(), request.getGender().toString(), null);
            UUID userId = UUID.fromString(user.getId());
            ActionResponse response = authGrpc.registerWithCredentials(userId, request);
            return ActionMapper.toResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<ActionResponseDto> sendResetPasswordCode(String email) {
        return Mono.fromCallable(()->{
            ActionResponse response = authGrpc.sendVerificationEmail(email, VerificationType.RESET_PASSWORD);
            return ActionMapper.toResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<ActionResponseDto> resetPassword(ResetPasswordRequestDto request) {
        return Mono.fromCallable(()->{
            ActionResponse response = authGrpc.resetPassword(request);
            return ActionMapper.toResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<GenerateAccessTokenResponseDto> generateAccessToken(String refreshToken) {
        return Mono.fromCallable(()->{
            if(!JwtUtils.isTokenValid(refreshToken)) {
                throw new BusinessException("Token is expired.");
            }

            UUID accountId = JwtUtils.extractId(refreshToken);
            AccountSummaryResponse account = authGrpc.getAccountById(accountId);

            UUID userId = UUID.fromString(account.getUserId());
            UserRole role = UserRoleMapper.fromString(account.getRole());

            String accessToken = JwtUtils.generateAccessToken(userId, role);
            return AuthMapper.toGenerateAccessTokenResponseDto(accessToken);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    private Mono<Map<String, Object>> sendRequest(String accessToken, String url) {
        return webClient
                .get()
                .uri(url)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {});
    }

    public Mono<OAuthUserInfo> getUserInfoWithGoogle(String accessToken) {
        String url = "https://www.googleapis.com/oauth2/v3/userinfo";
        return sendRequest(accessToken, url).map(response -> {
            return OAuthUserInfo.builder()
                    .id((String) response.get("sub"))
                    .name((String) response.get("name"))
                    .email((String) response.get("email"))
                    .picture((String) response.get("picture"))
                    .build();
        });
    }
}
