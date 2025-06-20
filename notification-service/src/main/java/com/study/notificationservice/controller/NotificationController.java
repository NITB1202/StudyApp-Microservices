package com.study.notificationservice.controller;

import com.study.notificationservice.entity.Invitation;
import com.study.notificationservice.entity.Notification;
import com.study.notificationservice.entity.TeamNotificationSettings;
import com.study.notificationservice.mapper.InvitationMapper;
import com.study.notificationservice.mapper.NotificationMapper;
import com.study.notificationservice.mapper.TeamNotificationSettingsMapper;
import com.study.notificationservice.service.DeviceTokenService;
import com.study.notificationservice.service.InvitationService;
import com.study.notificationservice.service.NotificationService;
import com.study.common.grpc.ActionResponse;
import com.study.notificationservice.grpc.*;
import com.study.notificationservice.service.TeamNotificationSettingsService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;
import java.util.UUID;

@GrpcService
@RequiredArgsConstructor
public class NotificationController extends NotificationServiceGrpc.NotificationServiceImplBase {
    private final InvitationService invitationService;
    private final NotificationService notificationService;
    private final DeviceTokenService deviceTokenService;
    private final TeamNotificationSettingsService settingsService;

    //Notifications
    @Override
    public void getNotifications(GetNotificationsRequest request, StreamObserver<NotificationsResponse> responseObserver) {
        UUID userId = UUID.fromString(request.getUserId());

        List<Notification> notifications = notificationService.getNotifications(request);
        long total = notificationService.countNotificationsByUserId(userId);
        String nextCursor = !notifications.isEmpty() && notifications.size() == request.getSize() ?
                notifications.get(notifications.size() - 1).getCreatedAt().toString() : "";

        NotificationsResponse response = NotificationMapper.toNotificationsResponse(notifications, total, nextCursor);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getUnreadNotificationCount(GetUnreadNotificationCountRequest request, StreamObserver<GetUnreadNotificationCountResponse> responseObserver) {
        int count = notificationService.getUnreadNotificationCount(request);

        GetUnreadNotificationCountResponse response = GetUnreadNotificationCountResponse.newBuilder()
                .setCount(count)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void markNotificationsAsRead(MarkNotificationsAsReadRequest request, StreamObserver<ActionResponse> responseObserver) {
        notificationService.markNotificationAsRead(request);

        ActionResponse response = ActionResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Mark successfully.")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void markAllNotificationsAsRead(MarkAllNotificationsAsReadRequest request, StreamObserver<ActionResponse> responseObserver) {
        notificationService.markAllNotificationsAsRead(request);

        ActionResponse response = ActionResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Mark successfully.")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void deleteNotifications(DeleteNotificationsRequest request, StreamObserver<ActionResponse> responseObserver) {
        notificationService.deleteNotifications(request);

        ActionResponse response = ActionResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Delete successfully.")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void deleteAllNotifications(DeleteAllNotificationsRequest request, StreamObserver<ActionResponse> responseObserver) {
        notificationService.deleteAllNotifications(request);

        ActionResponse response = ActionResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Delete successfully.")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    //Invitations
    @Override
    public void getInvitations(GetInvitationsRequest request, StreamObserver<InvitationsResponse> responseObserver) {
        UUID userId = UUID.fromString(request.getUserId());

        List<Invitation> invitations = invitationService.getInvitations(request);
        long total = invitationService.countInvitationsByUserId(userId);
        String nextCursor = !invitations.isEmpty() && invitations.size() == request.getSize() ?
                invitations.get(invitations.size() - 1).getInvitedAt().toString() : "";

        InvitationsResponse response = InvitationMapper.toInvitationsResponse(invitations, total, nextCursor);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void replyToInvitation(ReplyToInvitationRequest request, StreamObserver<ActionResponse> responseObserver) {
        invitationService.replyToInvitation(request);

        ActionResponse response = ActionResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Reply successfully.")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    //Device tokens
    @Override
    public void registerDeviceToken(RegisterDeviceTokenRequest request, StreamObserver<ActionResponse> responseObserver) {
        deviceTokenService.registerDeviceToken(request);

        ActionResponse response = ActionResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Register successfully.")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void removeDeviceToken(RemoveDeviceTokenRequest request, StreamObserver<ActionResponse> responseObserver) {
        deviceTokenService.removeDeviceToken(request);

        ActionResponse response = ActionResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Remove successfully.")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    //Team notification settings
    @Override
    public void getTeamNotificationSettings(GetTeamNotificationSettingsRequest request, StreamObserver<TeamNotificationSettingsResponse> responseObserver) {
        TeamNotificationSettings settings = settingsService.getTeamNotificationSettings(request);
        TeamNotificationSettingsResponse response = TeamNotificationSettingsMapper.toTeamNotificationSettingsResponse(settings);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void updateTeamNotificationSettings(UpdateTeamNotificationSettingsRequest request, StreamObserver<TeamNotificationSettingsResponse> responseObserver) {
        TeamNotificationSettings settings = settingsService.updateTeamNotificationSettings(request);
        TeamNotificationSettingsResponse response = TeamNotificationSettingsMapper.toTeamNotificationSettingsResponse(settings);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
