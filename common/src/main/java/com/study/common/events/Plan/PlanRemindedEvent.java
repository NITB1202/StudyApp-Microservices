package com.study.common.events.Plan;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanRemindedEvent {
    private UUID planId;
    private List<UUID> receiverIds;
}
