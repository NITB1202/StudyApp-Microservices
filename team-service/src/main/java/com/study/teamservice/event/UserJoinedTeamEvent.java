package com.study.teamservice.event;

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
