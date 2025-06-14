package com.study.apigateway.dto.Notification.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UnreadNotificationCountResponseDto {
    private int count;
}
