package com.study.common.events.Plan;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanRemindedEvent {
    private UUID planId;
    private String planName;
    private LocalDateTime endAt;
    private List<UUID> receiverIds;
}

//Message: Plan "PLAN01" will expire at 10:00:00 on 12/02/2025 -> Linked: planId
