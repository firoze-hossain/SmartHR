package com.roze.smarthr.service;

import com.roze.smarthr.dto.CreateNotificationDto;
import com.roze.smarthr.dto.NotificationCountDto;
import com.roze.smarthr.dto.NotificationDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NotificationService {
    NotificationDto createNotification(CreateNotificationDto dto);

    Page<NotificationDto> getUserNotifications(Long userId, Pageable pageable);

    List<NotificationDto> getUserUnreadNotifications(Long userId);

    NotificationCountDto getUserNotificationCount(Long userId);

    void markAsRead(Long notificationId, Long userId);

    void markAllAsRead(Long userId);

    void sendRealTimeNotification(Long userId, NotificationDto notification);

    void sendBulkNotifications(List<CreateNotificationDto> notifications);

    void cleanOldNotifications();
}
