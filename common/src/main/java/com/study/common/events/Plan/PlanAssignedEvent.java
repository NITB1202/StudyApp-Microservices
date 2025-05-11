package com.study.common.events.Plan;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanAssignedEvent {
    private UUID planId;
    private String planName;
    private List<UUID> assigneeIds;
}

//Message: You have been assigned for plan "PLAN01" -> Linked: planId
