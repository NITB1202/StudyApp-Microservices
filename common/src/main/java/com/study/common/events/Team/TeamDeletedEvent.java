package com.study.common.events.Team;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamDeletedEvent {
    private UUID teamId;
    private String teamName;
    private UUID deletedBy;
    private List<UUID> memberIds;
}

//Message: Team "TEAM01" has been deleted by User01
