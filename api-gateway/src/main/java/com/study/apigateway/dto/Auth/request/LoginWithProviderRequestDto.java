package com.study.apigateway.dto.Auth.request;

import com.study.common.enums.Provider;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginWithProviderRequestDto {
    @NotNull(message = "Provider is required.")
    private Provider provider;

    @NotNull(message = "Access token is required.")
    private String accessToken;
}
