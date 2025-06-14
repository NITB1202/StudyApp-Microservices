package com.study.apigateway.dto.Notification.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.study.common.enums.LinkedSubject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponseDto {
    private UUID id;

    private String title;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    private String content;

    private boolean isRead;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LinkedSubject subject;

    private UUID subjectId;
}
