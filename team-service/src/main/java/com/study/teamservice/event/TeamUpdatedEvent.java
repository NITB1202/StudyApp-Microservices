package com.study.teamservice.event;

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
    private UUID updatedBy;
    private String name;
    private String avatarUrl;
    private List<UUID> memberIds;
    private Set<String> updatedFields;
}