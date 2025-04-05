package com.study.teamservice.event;

import lombok.*;

import java.util.UUID;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvitationCreatedEvent {
    private UUID teamId;
    private UUID fromId;
    private UUID toId;
}
