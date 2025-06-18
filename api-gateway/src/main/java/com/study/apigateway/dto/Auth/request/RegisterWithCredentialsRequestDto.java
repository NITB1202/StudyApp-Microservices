package com.study.apigateway.dto.Auth.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterWithCredentialsRequestDto {
    @NotEmpty(message = "Name is required.")
    private String name;

    @NotEmpty(message = "Email is required.")
    @Email(message = "Invalid email format.")
    private String email;

    @NotEmpty(message = "Password is required.")
    private String password;

    @NotEmpty(message = "Verification code is required.")
    private String verificationCode;
}
