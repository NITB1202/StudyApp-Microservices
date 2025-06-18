package com.study.authservice.controller;

import com.study.authservice.entity.Account;
import com.study.authservice.grpc.*;
import com.study.authservice.mapper.AuthMapper;
import com.study.authservice.service.AuthService;
import com.study.authservice.service.CodeService;
import com.study.authservice.service.MailService;
import com.study.common.grpc.ActionResponse;
import com.study.common.utils.JwtUtils;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class AuthController extends AuthServiceGrpc.AuthServiceImplBase {
    private final AuthService authService;
    private final CodeService codeService;
    private final MailService mailService;

    @Override
    public void loginWithCredentials(LoginWithCredentialsRequest request, StreamObserver<LoginResponse> responseObserver) {
        Account account = authService.loginWithCredentials(request);
        String accessToken = JwtUtils.generateAccessToken(account.getUserId(), account.getRole());
        String refreshToken = JwtUtils.generateRefreshToken(account.getId());
        LoginResponse loginResponse = AuthMapper.toLoginResponse(accessToken, refreshToken);
        responseObserver.onNext(loginResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void loginWithProvider(LoginWithProviderRequest request, StreamObserver<LoginResponse> responseObserver) {
        Account account = authService.loginWithProvider(request);
        String accessToken = JwtUtils.generateAccessToken(account.getUserId(), account.getRole());
        String refreshToken = JwtUtils.generateRefreshToken(account.getId());
        LoginResponse loginResponse = AuthMapper.toLoginResponse(accessToken, refreshToken);
        responseObserver.onNext(loginResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void isAccountRegistered(IsAccountRegisteredRequest request, StreamObserver<IsAccountRegisteredResponse> responseObserver) {
        boolean isRegistered = authService.IsAccountRegistered(request);
        IsAccountRegisteredResponse response = AuthMapper.toIsAccountRegisteredResponse(isRegistered);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void validateRegisterInfo(ValidateRegisterInfoRequest request, StreamObserver<ActionResponse> responseObserver) {
        ActionResponse response = ActionResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Validate successfully. A verification code has been sent to the registered email.")
                .build();

        if(!authService.validateEmail(request.getEmail())) {
            response = ActionResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage("Email is already used.")
                    .build();
        }

        if(!authService.validatePassword(request.getPassword())) {
            response = ActionResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage(authService.getPasswordRule())
                    .build();
        }

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void sendVerificationEmail(SendVerificationEmailRequest request, StreamObserver<ActionResponse> responseObserver) {
        String code = codeService.generateCode(request.getEmail(), request.getType());
        mailService.sendVerificationEmail(request.getEmail(), code);

        ActionResponse response = ActionResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Verification email has been sent.")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void registerWithCredentials(RegisterWithCredentialsRequest request, StreamObserver<ActionResponse> responseObserver) {
        authService.registerWithCredentials(request);

        ActionResponse response = ActionResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Register successful.")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void registerWithProvider(RegisterWithProviderRequest request, StreamObserver<ActionResponse> responseObserver) {
        authService.registerWithProvider(request);

        ActionResponse response = ActionResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Register successful.")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void resetPassword(ResetPasswordRequest request, StreamObserver<ActionResponse> responseObserver) {
        authService.resetPassword(request);

        ActionResponse response = ActionResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Reset password successful.")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
