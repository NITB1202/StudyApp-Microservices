package com.study.apigateway.controller;

import com.study.apigateway.dto.Action.ActionResponseDto;
import com.study.apigateway.dto.Auth.request.*;
import com.study.apigateway.dto.Auth.response.GenerateAccessTokenResponseDto;
import com.study.apigateway.dto.Auth.response.LoginResponseDto;
import com.study.apigateway.exception.ErrorResponse;
import com.study.apigateway.service.Auth.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/cred")
    @Operation(summary = "Login with credentials.")
    @ApiResponse(responseCode = "200", description = "Login successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid request body.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<LoginResponseDto>> loginWithCredentials(@Valid @RequestBody LoginWithCredentialsRequestDto request) {
        return authService.loginWithCredentials(request).map(ResponseEntity::ok);
    }

    @PostMapping("/prov")
    @Operation(summary = "Login with OAuth provider (Google, Facebook).")
    @ApiResponse(responseCode = "200", description = "Login successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid request body.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<LoginResponseDto>> loginWithProvider(@Valid @RequestBody LoginWithProviderRequestDto request) {
        return authService.loginWithProvider(request).map(ResponseEntity::ok);
    }

    @PostMapping("/validate")
    @Operation(summary = "Validate account information before registering.")
    @ApiResponse(responseCode = "200", description = "Validate successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid request body.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<ActionResponseDto>> validateRegisterInfo(@Valid @RequestBody ValidateRegisterInfoRequestDto request) {
        return authService.validateRegisterInfo(request).map(ResponseEntity::ok);
    }

    @PostMapping("/register")
    @Operation(summary = "Register.")
    @ApiResponse(responseCode = "200", description = "Register successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid request body.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<ActionResponseDto>> registerWithCredentials(@Valid @RequestBody RegisterWithCredentialsRequestDto request) {
        return authService.registerWithCredentials(request).map(ResponseEntity::ok);
    }

    @GetMapping("/reset")
    @Operation(summary = "Send a verification code to the email address to reset the password.")
    @ApiResponse(responseCode = "200", description = "Send successfully.")
    public Mono<ResponseEntity<ActionResponseDto>> sendResetPasswordCode(@RequestParam String email) {
        return authService.sendResetPasswordCode(email).map(ResponseEntity::ok);
    }

    @PostMapping("/reset")
    @Operation(summary = "Reset the password.")
    @ApiResponse(responseCode = "200", description = "Reset successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid request body.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<ActionResponseDto>> resetPassword(@Valid @RequestBody ResetPasswordRequestDto request) {
        return authService.resetPassword(request).map(ResponseEntity::ok);
    }

    @GetMapping("/access")
    @Operation(summary = "Generate a new access token from refresh token.")
    @ApiResponse(responseCode = "200", description = "Generate successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid refresh token.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<GenerateAccessTokenResponseDto>> generateAccessToken(@RequestParam String refreshToken) {
        return authService.generateAccessToken(refreshToken).map(ResponseEntity::ok);
    }
}
