package com.study.common.events.Team;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLeftTeamEvent {
    private UUID teamId;
    private String teamName;
    private UUID userId;
    private List<UUID> memberIds;
}

//Message: User01 left team "TEAM01" -> Linked: teamId
