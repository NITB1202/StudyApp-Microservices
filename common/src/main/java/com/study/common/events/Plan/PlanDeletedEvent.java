package com.study.common.events.Plan;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanDeletedEvent {
    private UUID userId;
    private String planName;
    private List<UUID> assigneeIds;
}

//Message: User01 deleted "PLAN01"
