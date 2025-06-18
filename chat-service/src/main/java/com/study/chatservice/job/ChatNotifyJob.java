package com.study.chatservice.job;

import com.study.chatservice.entity.ChatNotification;
import com.study.chatservice.event.ChatEventPublisher;
import com.study.chatservice.service.ChatNotificationService;
import com.study.common.events.Chat.MessageSentEvent;
import lombok.RequiredArgsConstructor;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ChatNotifyJob implements Job {
    private final ChatNotificationService chatNotificationService;
    private final ChatEventPublisher publisher;
    private final static String SEND_TOPIC = "message-sent";

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap dataMap = context.getMergedJobDataMap();
        UUID teamId = UUID.fromString(dataMap.getString("teamId"));

        ChatNotification notification = chatNotificationService.getChatNotificationByTeamId(teamId);
        List<UUID> receiverIds = new ArrayList<>();

        MessageSentEvent event = MessageSentEvent.builder()
                .teamId(teamId)
                .teamName(notification.getTeamName())
                .teamAvatarUrl(notification.getTeamAvatarUrl())
                .newMessageCount(notification.getNewMessageCount())
                .receiverIds(receiverIds)
                .build();

        publisher.publishEvent(SEND_TOPIC, event);
    }
}
