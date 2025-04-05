package com.study.common.events.Notification;

import lombok.*;

import java.util.UUID;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvitationAcceptEvent {
    private UUID teamId;
    private UUID userId;
}
