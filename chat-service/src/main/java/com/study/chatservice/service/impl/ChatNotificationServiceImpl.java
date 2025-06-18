package com.study.chatservice.service.impl;

import com.study.chatservice.entity.ChatNotification;
import com.study.chatservice.grpc.TeamServiceGrpcClient;
import com.study.chatservice.job.ChatNotifyJob;
import com.study.chatservice.repository.ChatNotificationRepository;
import com.study.chatservice.service.ChatNotificationService;
import com.study.common.exceptions.BusinessException;
import com.study.common.exceptions.NotFoundException;
import com.study.teamservice.grpc.TeamDetailResponse;
import lombok.RequiredArgsConstructor;
import org.quartz.*;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatNotificationServiceImpl implements ChatNotificationService {
    private final TeamServiceGrpcClient teamServiceGrpcClient;
    private final ChatNotificationRepository chatNotificationRepository;
    private final Scheduler scheduler;
    private final static int PUSH_SECONDS = 10;

    @Override
    public void increaseNewMessageCount(UUID teamId) {
        ChatNotification notification = chatNotificationRepository.findById(teamId).orElse(null);

        if (notification == null) {
            TeamDetailResponse teamDetail = teamServiceGrpcClient.getTeamById(teamId);

            notification = ChatNotification.builder()
                    .teamId(teamId)
                    .teamName(teamDetail.getName())
                    .newMessageCount(1)
                    .build();

            //Schedule job
            JobDetail jobDetail = JobBuilder.newJob(ChatNotifyJob.class)
                    .withIdentity("reminder_" + teamId)
                    .usingJobData("teamId", teamId.toString())
                    .build();

            //Trigger after creating 10s
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("trigger_" + teamId)
                    .startAt(Timestamp.valueOf(LocalDateTime.now().plusSeconds(PUSH_SECONDS)))
                    .build();

            try {
                scheduler.scheduleJob(jobDetail, trigger);
            } catch (SchedulerException e) {
                throw new BusinessException(e.getMessage());
            }

        }
        else {
            notification.setNewMessageCount(notification.getNewMessageCount() + 1);
        }

        chatNotificationRepository.save(notification);
    }

    @Override
    public ChatNotification getChatNotificationByTeamId(UUID teamId) {
        return chatNotificationRepository.findById(teamId).orElseThrow(
                () -> new NotFoundException("Chat notification not found for teamId: " + teamId)
        );
    }

    @Override
    public void deleteChatNotification(UUID teamId) {
        chatNotificationRepository.deleteById(teamId);
    }

}
