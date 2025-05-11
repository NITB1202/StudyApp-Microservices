package com.study.common.events.Plan;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanCompletedEvent {
    private UUID planId;
    private String planName;
    private List<UUID> assigneeIds;
}

//Message: Plan "PLAN01" has been completed. -> Linked: planId
