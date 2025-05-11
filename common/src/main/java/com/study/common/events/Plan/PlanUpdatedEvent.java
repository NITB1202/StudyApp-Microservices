package com.study.common.events.Plan;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanUpdatedEvent {
    private UUID userId;
    private UUID planId;
    private String planName;
    private List<UUID> assigneeIds;
}

//Message: User01 just updated plan "PLAN01" -> Linked: planId
