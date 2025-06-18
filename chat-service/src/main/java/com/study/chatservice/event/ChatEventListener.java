package com.study.chatservice.event;

import com.study.chatservice.service.MessageService;
import com.study.common.events.Team.TeamDeletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatEventListener {
    private final MessageService messageService;

    @KafkaListener(topics = "team-deleted", groupId = "chat-team-deleted")
    public void consumeTeamDeletedEvent(TeamDeletedEvent event) {
        messageService.deleteAllMessagesInTeam(event.getTeamId());
    }
}
