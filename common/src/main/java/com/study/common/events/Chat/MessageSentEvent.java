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
    private String teamAvatarUrl;
    private int newMessageCount;
    private List<UUID> receiverIds;
}

//Message: 3 new messages in team 'TEAM01'.
