package com.study.apigateway.grpc;

import com.study.common.grpc.ActionResponse;
import com.study.notificationservice.grpc.*;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class NotificationGrpcClient {
    @GrpcClient("notification-service")
    private NotificationServiceGrpc.NotificationServiceBlockingStub blockingStub;

    //Notification
    public NotificationsResponse getNotifications(UUID userId, LocalDateTime cursor, int size) {
        String cursorStr = cursor != null ? cursor.toString() : "";

        GetNotificationsRequest request = GetNotificationsRequest.newBuilder()
                .setUserId(userId.toString())
                .setCursor(cursorStr)
                .setSize(size)
                .build();

        return blockingStub.getNotifications(request);
    }

    public GetUnreadNotificationCountResponse getUnreadNotificationCount(UUID userId) {
        GetUnreadNotificationCountRequest request = GetUnreadNotificationCountRequest.newBuilder()
                .setUserId(userId.toString())
                .build();

        return blockingStub.getUnreadNotificationCount(request);
    }

    public ActionResponse markNotificationAsRead(List<UUID> ids) {
        List<String> idsStr = ids.stream()
                .map(UUID::toString)
                .toList();

        MarkNotificationsAsReadRequest request = MarkNotificationsAsReadRequest.newBuilder()
                .addAllIds(idsStr)
                .build();

        return blockingStub.markNotificationsAsRead(request);
    }

    public ActionResponse markAllNotificationsAsRead(UUID userId) {
        MarkAllNotificationsAsReadRequest request = MarkAllNotificationsAsReadRequest.newBuilder()
                .setUserId(userId.toString())
                .build();

        return blockingStub.markAllNotificationsAsRead(request);
    }

    public ActionResponse deleteNotifications(List<UUID> ids) {
        List<String> idsStr = ids.stream()
                .map(UUID::toString)
                .toList();

        DeleteNotificationsRequest request = DeleteNotificationsRequest.newBuilder()
                .addAllIds(idsStr)
                .build();

        return blockingStub.deleteNotifications(request);
    }

    public ActionResponse deleteAllNotifications(UUID userId) {
        DeleteAllNotificationsRequest request = DeleteAllNotificationsRequest.newBuilder()
                .setUserId(userId.toString())
                .build();

        return blockingStub.deleteAllNotifications(request);
    }

    //Invitation
    public InvitationsResponse getInvitations(UUID userId, LocalDateTime cursor, int size) {
        String cursorStr = cursor != null ? cursor.toString() : "";

        GetInvitationsRequest request = GetInvitationsRequest.newBuilder()
                .setUserId(userId.toString())
                .setCursor(cursorStr)
                .setSize(size)
                .build();

        return blockingStub.getInvitations(request);
    }

    public ActionResponse replyToInvitation(UUID id, boolean accept) {
        ReplyToInvitationRequest request = ReplyToInvitationRequest.newBuilder()
                .setId(id.toString())
                .setAccept(accept)
                .build();

        return blockingStub.replyToInvitation(request);
    }

    //Device token
    public ActionResponse registerDeviceToken(UUID userId, String fcmToken) {
        RegisterDeviceTokenRequest request = RegisterDeviceTokenRequest.newBuilder()
                .setUserId(userId.toString())
                .setFcmToken(fcmToken)
                .build();

        return blockingStub.registerDeviceToken(request);
    }

    public ActionResponse removeDeviceToken(String fcmToken) {
        RemoveDeviceTokenRequest request = RemoveDeviceTokenRequest.newBuilder()
                .setFcmToken(fcmToken)
                .build();

        return blockingStub.removeDeviceToken(request);
    }
}
