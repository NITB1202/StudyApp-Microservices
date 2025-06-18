package com.study.common.events.Chat;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageSentEvent {
    private UUID teamId;
    private String teamName;
    private int newMessageCount;
    private List<UUID> receiverIds;
}

//Message: You have 3 new messages from team 'TEAM 01'.
