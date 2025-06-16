package com.study.documentservice.event;

import com.study.common.events.Team.TeamCreatedEvent;
import com.study.common.events.Team.TeamDeletedEvent;
import com.study.common.events.User.UserCreatedEvent;
import com.study.documentservice.service.DocumentService;
import com.study.documentservice.service.FolderService;
import com.study.documentservice.service.UsageService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DocumentEventListener {
    private final UsageService usageService;
    private final FolderService folderService;
    private final DocumentService documentService;

    @KafkaListener(topics = "user-created", groupId = "document-user-created")
    public void consumeUserCreatedEvent(UserCreatedEvent event) {
        usageService.createUserUsage(event.getUserId());
    }

    @KafkaListener(topics = "team-created", groupId = "document-team-created")
    public void consumeTeamCreatedEvent(TeamCreatedEvent event) {
        usageService.createTeamUsage(event.getTeamId());
    }

    @KafkaListener(topics = "team-deleted", groupId = "document-team-deleted")
    public void consumeTeamDeletedEvent(TeamDeletedEvent event) {
        List<UUID> folderIds = folderService.getAllIdsByTeamId(event.getTeamId());

        for (UUID id : folderIds) {
            documentService.deleteAllDocuments(id);
        }

        folderService.deleteAllById(folderIds);
        usageService.deleteTeamUsage(event.getTeamId());
    }
}
