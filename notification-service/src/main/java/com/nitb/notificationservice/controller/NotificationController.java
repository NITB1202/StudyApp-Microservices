package com.nitb.notificationservice.controller;

import com.nitb.notificationservice.service.InvitationService;
import com.nitb.notificationservice.service.NotificationService;
import com.nitb.notificationservice.service.TeamNotificationSettingsService;
import com.study.common.grpc.ActionResponse;
import com.study.notificationservice.grpc.*;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class NotificationController extends NotificationServiceGrpc.NotificationServiceImplBase {
    private final InvitationService invitationService;
    private final NotificationService notificationService;
    private final TeamNotificationSettingsService teamNotificationSettingsService;

    //Notifications
    @Override
    public void getNotifications(GetNotificationsRequest request, StreamObserver<NotificationsResponse> responseObserver) {

    }

    @Override
    public void markNotificationsAsRead(MarkNotificationsAsReadRequest request, StreamObserver<ActionResponse> responseObserver) {

    }

    @Override
    public void markAllNotificationsAsRead(MarkAllNotificationsAsReadRequest request, StreamObserver<ActionResponse> responseObserver) {

    }

    @Override
    public void deleteNotifications(DeleteNotificationsRequest request, StreamObserver<ActionResponse> responseObserver) {

    }

    @Override
    public void deleteAllNotifications(DeleteAllNotificationsRequest request, StreamObserver<ActionResponse> responseObserver) {

    }

    //Invitations
    @Override
    public void getInvitations(GetInvitationsRequest request, StreamObserver<InvitationsResponse> responseObserver) {

    }

    @Override
    public void replyToInvitation(ReplyToInvitationRequest request, StreamObserver<ActionResponse> responseObserver) {

    }

    //Team notification settings
    public void getTeamNotificationSettings(GetTeamNotificationSettingsRequest request, StreamObserver<TeamNotificationSettingsResponse> responseObserver) {

    }

    public void updateTeamNotificationSettings(UpdateTeamNotificationSettingsRequest request, StreamObserver<ActionResponse> responseObserver) {

    }
}
