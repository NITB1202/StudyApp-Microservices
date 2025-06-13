package com.study.common.events.Team;

import lombok.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamUpdatedEvent {
    private UUID id;
    private String teamName;
    private UUID updatedBy;
    private List<UUID> memberIds;
    private Set<String> updatedFields;
}

//Message: User01 has updated the team "TEAM01"'s general information. -> Linked: teamId