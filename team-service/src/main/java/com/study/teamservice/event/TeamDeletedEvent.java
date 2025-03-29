package com.study.teamservice.event;

import lombok.*;

import java.util.UUID;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamDeletedEvent {
    private UUID id;
    private UUID deletedBy;
}
