package com.nitb.notificationservice.dto;

import com.study.common.enums.LinkedSubject;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateNotificationDto {
    private UUID userId;

    private String title;

    private String content;

    private LinkedSubject subject;

    private UUID subjectId;
}
