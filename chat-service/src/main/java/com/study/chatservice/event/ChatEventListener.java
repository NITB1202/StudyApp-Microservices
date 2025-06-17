package com.study.chatservice.event;

import com.study.chatservice.service.MessageReadStatusService;
import com.study.chatservice.service.MessageService;
import com.study.common.events.Team.TeamDeletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatEventListener {
    private final MessageService messageService;
    private final MessageReadStatusService statusService;

    @KafkaListener(topics = "team-deleted", groupId = "chat-team-deleted")
    public void consumeTeamDeletedEvent(TeamDeletedEvent event) {
        List<UUID> messageIds = messageService.getAllTeamMessageIds(event.getTeamId());

        for (UUID messageId : messageIds) {
            statusService.deleteAllReadStatus(messageId);
        }

        messageService.deleteAllMessagesInTeam(event.getTeamId());
    }
}
