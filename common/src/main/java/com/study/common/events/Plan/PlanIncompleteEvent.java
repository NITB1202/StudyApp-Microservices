package com.study.common.events.Plan;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanIncompleteEvent {
    private UUID userId;
    private UUID planId;
    private String planName;
    private List<UUID> assigneeIds;
}

//Message: Plan "PLAN01" has been moved back to "In Progress" as User01 updated a task to incomplete. -> Linked: planId