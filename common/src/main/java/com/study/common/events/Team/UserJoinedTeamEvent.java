package com.study.common.events.Team;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserJoinedTeamEvent {
    private UUID userId;
    private UUID teamId;
    private List<UUID> memberIds;
}
