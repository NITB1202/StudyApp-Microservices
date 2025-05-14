package com.study.common.events.Notification;

import lombok.*;

import java.util.UUID;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvitationCreatedEvent {
    private UUID teamId;
    private String teamName;
    private UUID fromId;
    private UUID toId;
}

//Message: User01 has invited you to the team "TEAM01" -> Linked: teamId
