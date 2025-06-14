package com.study.common.events.Team;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamCreatedEvent {
    private UUID teamId;
    private UUID creatorId;
}
