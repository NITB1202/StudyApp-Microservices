package com.nitb.notificationservice.service.impl;

import com.nitb.notificationservice.dto.CreateNotificationRequestDto;
import com.nitb.notificationservice.entity.Notification;
import com.nitb.notificationservice.repository.NotificationRepository;
import com.nitb.notificationservice.service.NotificationService;
import com.study.common.exceptions.NotFoundException;
import com.study.notificationservice.grpc.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private static final int DEFAULT_SIZE = 10;

    @Override
    public void createNotification(CreateNotificationRequestDto request) {
        Notification notification = Notification.builder()
                .userId(request.getUserId())
                .title(request.getTitle())
                .content(request.getContent())
                .createdAt(LocalDateTime.now())
                .isRead(false)
                .subject(request.getSubject())
                .subjectId(request.getSubjectId())
                .build();

        notificationRepository.save(notification);
    }

    @Override
    public List<Notification> getNotifications(GetNotificationsRequest request) {
        UUID userId = UUID.fromString(request.getUserId());
        int size = request.getSize() > 0 ? request.getSize() : DEFAULT_SIZE;

        Pageable pageable = PageRequest.of(0, size, Sort.by("createdAt").descending());

        if(request.getCursor().isEmpty()) {
            return notificationRepository.findByUserId(userId, pageable);
        }

        LocalDateTime cursor = LocalDateTime.parse(request.getCursor());
        return notificationRepository.findByUserIdAndCreatedAtLessThan(userId, cursor, pageable);
    }

    @Override
    @Transactional
    public void markNotificationAsRead(MarkNotificationsAsReadRequest request) {
        List<Notification> savedNotifications = new ArrayList<>();

        for(String idStr : request.getIdsList()) {
            UUID id = UUID.fromString(idStr);
            Notification notification = notificationRepository.findById(id).orElseThrow(
                    () -> new NotFoundException("Notification with id " + id + " not found.")
            );

            notification.setIsRead(true);
            savedNotifications.add(notification);
        }

        notificationRepository.saveAll(savedNotifications);
    }

    @Override
    public void markAllNotificationsAsRead(MarkAllNotificationsAsReadRequest request) {
        UUID userId = UUID.fromString(request.getUserId());

        List<Notification> notifications = notificationRepository.findByUserId(userId);

        for(Notification notification : notifications) {
            notification.setIsRead(true);
        }

        notificationRepository.saveAll(notifications);
    }

    @Override
    public void deleteNotifications(DeleteNotificationsRequest request) {
        List<UUID> ids = request.getIdsList().stream()
                .map(UUID::fromString)
                .toList();

        List<Notification> notifications = notificationRepository.findAllById(ids);
        notificationRepository.deleteAll(notifications);
    }

    @Override
    public void deleteAllNotifications(DeleteAllNotificationsRequest request) {
        UUID userId = UUID.fromString(request.getUserId());
        List<Notification> notifications = notificationRepository.findByUserId(userId);
        notificationRepository.deleteAll(notifications);
    }

    @Override
    public void deleteNotificationBefore(LocalDateTime dateTime) {
        notificationRepository.deleteByCreatedAtBefore(dateTime);
    }

    @Override
    public long countNotificationsByUserId(UUID userId) {
        return notificationRepository.countByUserId(userId);
    }
}
