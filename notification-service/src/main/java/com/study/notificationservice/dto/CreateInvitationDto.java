package com.study.notificationservice.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateInvitationDto {
    private String inviterName;

    private String inviterAvatarUrl;

    private UUID inviteeId;

    private UUID teamId;

    private String teamName;
}
