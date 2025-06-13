package com.nitb.notificationservice.event;

import com.nitb.notificationservice.dto.CreateInvitationDto;
import com.nitb.notificationservice.dto.CreateNotificationDto;
import com.nitb.notificationservice.grpc.UserServiceGrpcClient;
import com.nitb.notificationservice.service.InvitationService;
import com.nitb.notificationservice.service.NotificationService;
import com.study.common.enums.LinkedSubject;
import com.study.common.events.Notification.InvitationCreatedEvent;
import com.study.common.events.Plan.*;

import com.study.userservice.grpc.UserDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationEventListener {
    private final UserServiceGrpcClient userServiceGrpcClient;
    private final NotificationService notificationService;
    private final InvitationService invitationService;

    @KafkaListener(topics = "invitation-created", groupId = "notification-service-group")
    public void consumeInvitationCreatedEvent(InvitationCreatedEvent event) {
        UserDetailResponse inviter = userServiceGrpcClient.getUserById(event.getFromId());

        CreateInvitationDto dto = CreateInvitationDto.builder()
                .inviterName(inviter.getUsername())
                .inviterAvatarUrl(inviter.getAvatarUrl())
                .inviteeId(event.getToId())
                .teamId(event.getTeamId())
                .teamName(event.getTeamName())
                .build();

        invitationService.createInvitation(dto);
    }

    @KafkaListener(topics = "plan-assigned", groupId = "notification-service-group")
    public void consumePlanAssignedEvent(PlanAssignedEvent event) {
        String title = "New plan assigned";
        String content = "You have been assigned for plan '" + event.getPlanName() + "'.";

        for(UUID assigneeId : event.getAssigneeIds()) {
            CreateNotificationDto dto = CreateNotificationDto.builder()
                    .userId(assigneeId)
                    .title(title)
                    .content(content)
                    .subject(LinkedSubject.PLAN)
                    .subjectId(event.getPlanId())
                    .build();

            notificationService.createNotification(dto);
        }
    }

    @KafkaListener(topics = "plan-completed", groupId = "notification-service-group")
    public void consumePlanCompletedEvent(PlanCompletedEvent event) {
        String title = "Plan completed";
        String content = "Plan '" + event.getPlanName() + "' has been completed.";

        for(UUID assigneeId : event.getAssigneeIds()) {
            CreateNotificationDto dto = CreateNotificationDto.builder()
                    .userId(assigneeId)
                    .title(title)
                    .content(content)
                    .subject(LinkedSubject.PLAN)
                    .subjectId(event.getPlanId())
                    .build();

            notificationService.createNotification(dto);
        }
    }

    @KafkaListener(topics = "plan-deleted", groupId = "notification-service-group")
    public void consumePlanDeletedEvent(PlanDeletedEvent event) {
        UserDetailResponse user = userServiceGrpcClient.getUserById(event.getUserId());
        String title = "Plan deleted";
        String content = user.getUsername() + " deleted plan '" + event.getPlanName() +"'.";

        for(UUID assigneeId : event.getAssigneeIds()) {
            CreateNotificationDto dto = CreateNotificationDto.builder()
                    .userId(assigneeId)
                    .title(title)
                    .content(content)
                    .subject(LinkedSubject.PLAN)
                    .subjectId(null)
                    .build();

            notificationService.createNotification(dto);
        }
    }

    @KafkaListener(topics = "plan-incomplete", groupId = "notification-service-group")
    public void consumePlanIncompleteEvent(PlanIncompleteEvent event) {
        UserDetailResponse user = userServiceGrpcClient.getUserById(event.getUserId());
        String title = "Incomplete plan";
        String content = "Plan '" + event.getPlanName() + "' has been moved back to 'In progress' as " + user.getUsername() + " updated tasks.";

        for(UUID assigneeId : event.getAssigneeIds()) {
            CreateNotificationDto dto = CreateNotificationDto.builder()
                    .userId(assigneeId)
                    .title(title)
                    .content(content)
                    .subject(LinkedSubject.PLAN)
                    .subjectId(event.getPlanId())
                    .build();

            notificationService.createNotification(dto);
        }
    }

    @KafkaListener(topics = "plan-reminded", groupId = "notification-service-group")
    public void consumePlanRemindedEvent(PlanRemindedEvent event) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yy");

        String title = now.isBefore(event.getEndAt()) ? "Plan reminded" : "Expired plan";
        String content = now.isBefore(event.getEndAt()) ?
                "Plan '" + event.getPlanName() +"' will expire at " + event.getEndAt().format(timeFormatter) +
                        " on " + event.getEndAt().format(dateFormatter) + "." :
                "Plan '" + event.getPlanName() + "' has expired.";

        for(UUID receiverId : event.getReceiverIds()) {
            CreateNotificationDto dto = CreateNotificationDto.builder()
                    .userId(receiverId)
                    .title(title)
                    .content(content)
                    .subject(LinkedSubject.PLAN)
                    .subjectId(event.getPlanId())
                    .build();

            notificationService.createNotification(dto);
        }
    }

    @KafkaListener(topics = "plan-restored", groupId = "notification-service-group")
    public void consumePlanRestoredEvent(PlanRestoredEvent event) {
        UserDetailResponse user = userServiceGrpcClient.getUserById(event.getUserId());
        String title = "Plan restored";
        String content = user.getUsername() + " has restored plan '" + event.getPlanName() +"'.";

        for(UUID assigneeId : event.getAssigneeIds()) {
            CreateNotificationDto dto = CreateNotificationDto.builder()
                    .userId(assigneeId)
                    .title(title)
                    .content(content)
                    .subject(LinkedSubject.PLAN)
                    .subjectId(event.getPlanId())
                    .build();

            notificationService.createNotification(dto);
        }
    }

    @KafkaListener(topics = "plan-updated", groupId = "notification-service-group")
    public void consumePlanUpdatedEvent(PlanUpdatedEvent event) {
        UserDetailResponse user = userServiceGrpcClient.getUserById(event.getUserId());
        String title = "Plan updated";
        String content = user.getUsername() + " just updated plan '" + event.getPlanName() +"'.";

        for(UUID assigneeId : event.getAssigneeIds()) {
            CreateNotificationDto dto = CreateNotificationDto.builder()
                    .userId(assigneeId)
                    .title(title)
                    .content(content)
                    .subject(LinkedSubject.PLAN)
                    .subjectId(event.getPlanId())
                    .build();

            notificationService.createNotification(dto);
        }
    }
}
