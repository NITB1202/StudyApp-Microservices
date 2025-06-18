package com.study.apigateway.grpc;

import com.study.apigateway.dto.Auth.request.LoginWithCredentialsRequestDto;
import com.study.apigateway.dto.Auth.request.RegisterWithCredentialsRequestDto;
import com.study.apigateway.dto.Auth.request.ResetPasswordRequestDto;
import com.study.apigateway.dto.Auth.request.ValidateRegisterInfoRequestDto;
import com.study.authservice.grpc.*;
import com.study.common.enums.Provider;
import com.study.common.grpc.ActionResponse;
import com.study.common.mappers.ProviderMapper;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthServiceGrpcClient {
    @GrpcClient("auth-service")
    private AuthServiceGrpc.AuthServiceBlockingStub blockingStub;

    public LoginResponse loginWithCredentials(LoginWithCredentialsRequestDto dto) {
        LoginWithCredentialsRequest request = LoginWithCredentialsRequest.newBuilder()
                .setEmail(dto.getEmail())
                .setPassword(dto.getPassword())
                .build();

        return blockingStub.loginWithCredentials(request);
    }

    public LoginResponse loginWithProvider(String providerId, Provider provider) {
        SupportedProvider supportedProvider = ProviderMapper.toSupportedProvider(provider);

        LoginWithProviderRequest request = LoginWithProviderRequest.newBuilder()
                .setProviderId(providerId)
                .setProvider(supportedProvider)
                .build();

        return blockingStub.loginWithProvider(request);
    }

    public IsAccountRegisteredResponse isAccountRegistered(String providerId) {
        IsAccountRegisteredRequest request = IsAccountRegisteredRequest.newBuilder()
                .setProviderId(providerId)
                .build();

        return blockingStub.isAccountRegistered(request);
    }

    public ActionResponse validateRegisterInfo(ValidateRegisterInfoRequestDto dto) {
        ValidateRegisterInfoRequest request = ValidateRegisterInfoRequest.newBuilder()
                .setEmail(dto.getEmail())
                .setPassword(dto.getPassword())
                .build();

        return blockingStub.validateRegisterInfo(request);
    }

    public ActionResponse sendVerificationEmail(String email, VerificationType type) {
        SendVerificationEmailRequest request = SendVerificationEmailRequest.newBuilder()
                .setEmail(email)
                .setType(type)
                .build();

        return blockingStub.sendVerificationEmail(request);
    }

    public ActionResponse registerWithCredentials(UUID userId, RegisterWithCredentialsRequestDto dto) {
        RegisterWithCredentialsRequest request = RegisterWithCredentialsRequest.newBuilder()
                .setUserId(userId.toString())
                .setEmail(dto.getEmail())
                .setPassword(dto.getPassword())
                .setVerificationCode(dto.getVerificationCode())
                .build();

        return blockingStub.registerWithCredentials(request);
    }

    public ActionResponse registerWithProvider(String providerId, Provider provider, UUID userId, String email) {
        SupportedProvider supportedProvider = ProviderMapper.toSupportedProvider(provider);

        RegisterWithProviderRequest request = RegisterWithProviderRequest.newBuilder()
                .setProviderId(providerId)
                .setProvider(supportedProvider)
                .setUserId(userId.toString())
                .setEmail(email)
                .build();

        return blockingStub.registerWithProvider(request);
    }

    public ActionResponse resetPassword(ResetPasswordRequestDto dto) {
        ResetPasswordRequest request = ResetPasswordRequest.newBuilder()
                .setVerificationCode(dto.getVerificationCode())
                .setEmail(dto.getEmail())
                .setNewPassword(dto.getNewPassword())
                .build();

        return blockingStub.resetPassword(request);
    }
}
