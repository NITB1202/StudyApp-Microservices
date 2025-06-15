package com.study.apigateway.dto.Notification.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDeviceTokenRequestDto {
    @NotEmpty(message = "Fcm token is required.")
    private String fcmToken;
}
