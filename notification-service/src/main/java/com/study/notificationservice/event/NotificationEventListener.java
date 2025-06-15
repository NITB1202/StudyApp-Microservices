package com.study.notificationservice.event;

import com.study.common.events.Team.*;
import com.study.notificationservice.dto.CreateInvitationDto;
import com.study.notificationservice.dto.CreateNotificationDto;
import com.study.notificationservice.grpc.UserServiceGrpcClient;
import com.study.notificationservice.service.DeviceTokenService;
import com.study.notificationservice.service.InvitationService;
import com.study.notificationservice.service.NotificationService;
import com.study.common.enums.LinkedSubject;
import com.study.common.events.Notification.InvitationCreatedEvent;
import com.study.common.events.Plan.*;

import com.study.notificationservice.service.TeamNotificationSettingsService;
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
    private final DeviceTokenService deviceTokenService;
    private final TeamNotificationSettingsService settingsService;

    @KafkaListener(topics = "invitation-created", groupId = "notification-invitation-created")
    public void consumeInvitationCreatedEvent(InvitationCreatedEvent event) {
        UserDetailResponse inviter = userServiceGrpcClient.getUserById(event.getFromId());

        CreateInvitationDto dto = CreateInvitationDto.builder()
                .inviterName(inviter.getUsername())
                .inviterAvatarUrl(inviter.getAvatarUrl())
                .inviteeId(event.getToId())
                .teamId(event.getTeamId())
                .teamName(event.getTeamName())
                .build();

        UUID invitationId = invitationService.createInvitation(dto);

        String title = "Team invitation";
        String content = inviter.getUsername() + " has invited you to the team '" + event.getTeamName() + "'.";

        CreateNotificationDto notification = CreateNotificationDto.builder()
                .userId(event.getToId())
                .title(title)
                .content(content)
                .subject(LinkedSubject.INVITATION)
                .subjectId(invitationId)
                .build();

        deviceTokenService.sendPushNotification(notification);
    }

    @KafkaListener(topics = "plan-assigned", groupId = "notification-plan-assigned")
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
            deviceTokenService.sendPushNotification(dto);
        }
    }

    @KafkaListener(topics = "plan-completed", groupId = "notification-plan-completed")
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
            deviceTokenService.sendPushNotification(dto);
        }
    }

    @KafkaListener(topics = "plan-deleted", groupId = "notification-plan-deleted")
    public void consumePlanDeletedEvent(PlanDeletedEvent event) {
        UserDetailResponse user = userServiceGrpcClient.getUserById(event.getUserId());
        String title = "Plan deleted";
        String content = user.getUsername() + " deleted plan '" + event.getPlanName() +"'.";

        for(UUID assigneeId : event.getAssigneeIds()) {
            if(event.getUserId().equals(assigneeId)) continue;

            CreateNotificationDto dto = CreateNotificationDto.builder()
                    .userId(assigneeId)
                    .title(title)
                    .content(content)
                    .subject(LinkedSubject.PLAN)
                    .subjectId(null)
                    .build();

            notificationService.createNotification(dto);
            deviceTokenService.sendPushNotification(dto);
        }
    }

    @KafkaListener(topics = "plan-incomplete", groupId = "notification-plan-incomplete")
    public void consumePlanIncompleteEvent(PlanIncompleteEvent event) {
        UserDetailResponse user = userServiceGrpcClient.getUserById(event.getUserId());
        String title = "Incomplete plan";
        String content = "Plan '" + event.getPlanName() + "' has been moved back to 'In progress' as " + user.getUsername() + " updated tasks.";

        for(UUID assigneeId : event.getAssigneeIds()) {
            if(event.getUserId().equals(assigneeId)) continue;

            CreateNotificationDto dto = CreateNotificationDto.builder()
                    .userId(assigneeId)
                    .title(title)
                    .content(content)
                    .subject(LinkedSubject.PLAN)
                    .subjectId(event.getPlanId())
                    .build();

            notificationService.createNotification(dto);
            deviceTokenService.sendPushNotification(dto);
        }
    }

    @KafkaListener(topics = "plan-reminded", groupId = "notification-plan-reminded")
    public void consumePlanRemindedEvent(PlanRemindedEvent event) {
        boolean isReminder = LocalDateTime.now().isBefore(event.getEndAt());
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yy");

        String title = isReminder ? "Plan reminded" : "Expired plan";
        String content = isReminder ?
                "Plan '" + event.getPlanName() +"' will expire at " + event.getEndAt().format(timeFormatter) +
                        " on " + event.getEndAt().format(dateFormatter) + "." :
                "Plan '" + event.getPlanName() + "' has expired.";

        for(UUID receiverId : event.getReceiverIds()) {
            if(isReminder && event.getTeamId() != null && !settingsService.getTeamPlanReminder(event.getTeamId(), receiverId)) continue;

            CreateNotificationDto dto = CreateNotificationDto.builder()
                    .userId(receiverId)
                    .title(title)
                    .content(content)
                    .subject(LinkedSubject.PLAN)
                    .subjectId(event.getPlanId())
                    .build();

            notificationService.createNotification(dto);
            deviceTokenService.sendPushNotification(dto);
        }
    }

    @KafkaListener(topics = "plan-restored", groupId = "notification-plan-restored")
    public void consumePlanRestoredEvent(PlanRestoredEvent event) {
        UserDetailResponse user = userServiceGrpcClient.getUserById(event.getUserId());
        String title = "Plan restored";
        String content = user.getUsername() + " has restored plan '" + event.getPlanName() +"'.";

        for(UUID assigneeId : event.getAssigneeIds()) {
            if(event.getUserId().equals(assigneeId)) continue;

            CreateNotificationDto dto = CreateNotificationDto.builder()
                    .userId(assigneeId)
                    .title(title)
                    .content(content)
                    .subject(LinkedSubject.PLAN)
                    .subjectId(event.getPlanId())
                    .build();

            notificationService.createNotification(dto);
            deviceTokenService.sendPushNotification(dto);
        }
    }

    @KafkaListener(topics = "plan-updated", groupId = "notification-plan-updated")
    public void consumePlanUpdatedEvent(PlanUpdatedEvent event) {
        UserDetailResponse user = userServiceGrpcClient.getUserById(event.getUserId());
        String title = "Plan updated";
        String content = user.getUsername() + " just updated plan '" + event.getPlanName() +"'.";

        for(UUID assigneeId : event.getAssigneeIds()) {
            if(event.getUserId().equals(assigneeId)) continue;

            CreateNotificationDto dto = CreateNotificationDto.builder()
                    .userId(assigneeId)
                    .title(title)
                    .content(content)
                    .subject(LinkedSubject.PLAN)
                    .subjectId(event.getPlanId())
                    .build();

            notificationService.createNotification(dto);
            deviceTokenService.sendPushNotification(dto);
        }
    }

    @KafkaListener(topics = "team-created", groupId = "notification-team-created")
    public void consumeTeamCreatedEvent(TeamCreatedEvent event) {
        settingsService.createTeamNotificationSettings(event.getCreatorId(), event.getTeamId());
    }

    @KafkaListener(topics = "team-deleted", groupId = "notification-team-deleted")
    public void consumeTeamDeletedEvent(TeamDeletedEvent event) {
        UserDetailResponse deletedBy = userServiceGrpcClient.getUserById(event.getDeletedBy());
        String title = "Team deleted";
        String content = "Team '" + event.getTeamName() +"' has been deleted by " + deletedBy.getUsername() + ".";

        for(UUID memberId : event.getMemberIds()) {
            if(event.getDeletedBy().equals(memberId)) continue;

            CreateNotificationDto dto = CreateNotificationDto.builder()
                    .userId(memberId)
                    .title(title)
                    .content(content)
                    .subject(LinkedSubject.TEAM)
                    .subjectId(null)
                    .build();

            notificationService.createNotification(dto);
            deviceTokenService.sendPushNotification(dto);
        }

        settingsService.deleteAllByTeamId(event.getTeamId());
    }

    @KafkaListener(topics = "team-updated", groupId = "notification-team-updated")
    public void consumeTeamUpdatedEvent(TeamUpdatedEvent event) {
        UserDetailResponse updatedBy = userServiceGrpcClient.getUserById(event.getUpdatedBy());
        String title = "Team updated";
        String content = updatedBy.getUsername() + " has updated team '" + event.getTeamName() +"''s general information.";

        for(UUID memberId : event.getMemberIds()) {
            if(event.getUpdatedBy().equals(memberId)) continue;
            if(!settingsService.getTeamNotification(event.getId(), memberId)) continue;

            CreateNotificationDto dto = CreateNotificationDto.builder()
                    .userId(memberId)
                    .title(title)
                    .content(content)
                    .subject(LinkedSubject.TEAM)
                    .subjectId(event.getId())
                    .build();

            notificationService.createNotification(dto);
            deviceTokenService.sendPushNotification(dto);
        }
    }

    @KafkaListener(topics = "user-joined", groupId = "notification-user-joined")
    public void consumeUserJoinedTeamEvent(UserJoinedTeamEvent event) {
        UserDetailResponse user = userServiceGrpcClient.getUserById(event.getUserId());
        String title = "New team member";
        String content = user.getUsername() + " has joined team '" + event.getTeamName() +"'.";

        for(UUID memberId : event.getMemberIds()) {
            if(event.getUserId().equals(memberId)) continue;
            if(!settingsService.getTeamNotification(event.getTeamId(), memberId)) continue;

            CreateNotificationDto dto = CreateNotificationDto.builder()
                    .userId(memberId)
                    .title(title)
                    .content(content)
                    .subject(LinkedSubject.TEAM)
                    .subjectId(event.getTeamId())
                    .build();

            notificationService.createNotification(dto);
            deviceTokenService.sendPushNotification(dto);
        }

        settingsService.createTeamNotificationSettings(event.getUserId(), event.getTeamId());
    }

    @KafkaListener(topics = "user-left", groupId = "notification-user-left")
    public void consumeUserLeftTeamEvent(UserLeftTeamEvent event) {
        UserDetailResponse user = userServiceGrpcClient.getUserById(event.getUserId());
        String title = "Member left";
        String content = user.getUsername() + " has left team '" + event.getTeamName() +"'.";

        for(UUID memberId : event.getMemberIds()) {
            if(event.getUserId().equals(memberId)) continue;
            if(!settingsService.getTeamNotification(event.getTeamId(), memberId)) continue;

            CreateNotificationDto dto = CreateNotificationDto.builder()
                    .userId(memberId)
                    .title(title)
                    .content(content)
                    .subject(LinkedSubject.TEAM)
                    .subjectId(event.getTeamId())
                    .build();

            notificationService.createNotification(dto);
            deviceTokenService.sendPushNotification(dto);
        }

        settingsService.deleteByTeamIdAndUserId(event.getTeamId(), event.getUserId());
    }
}
