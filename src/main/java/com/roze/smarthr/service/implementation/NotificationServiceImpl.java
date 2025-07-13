package com.roze.smarthr.service.implementation;

import com.roze.smarthr.dto.CreateNotificationDto;
import com.roze.smarthr.dto.NotificationCountDto;
import com.roze.smarthr.dto.NotificationDto;
import com.roze.smarthr.entity.Notification;
import com.roze.smarthr.entity.User;
import com.roze.smarthr.exception.NotificationException;
import com.roze.smarthr.exception.ResourceNotFoundException;
import com.roze.smarthr.mapper.NotificationMapper;
import com.roze.smarthr.repository.NotificationRepository;
import com.roze.smarthr.repository.UserRepository;
import com.roze.smarthr.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final NotificationMapper notificationMapper;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    @Transactional
    public NotificationDto createNotification(CreateNotificationDto dto) {
        try {
            User recipient = userRepository.findById(dto.getRecipientId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));

            Notification notification = Notification.builder()
                    .recipient(recipient)
                    .title(dto.getTitle())
                    .message(dto.getMessage())
                    .type(dto.getType())
                    .priority(dto.getPriority())
                    .actionUrl(dto.getActionUrl())
                    .build();

            Notification savedNotification = notificationRepository.save(notification);
            NotificationDto notificationDto = notificationMapper.toDto(savedNotification);

            sendRealTimeNotification(recipient.getId(), notificationDto);
            return notificationDto;

        } catch (Exception e) {
            log.error("Failed to create notification: {}", e.getMessage());
            throw new NotificationException("Failed to create notification", e);
        }
    }

    @Override
    public Page<NotificationDto> getUserNotifications(Long userId, Pageable pageable) {
        User user = userRepository.getReferenceById(userId);
        return notificationRepository.findByRecipientOrderByCreatedAtDesc(user, pageable)
                .map(notificationMapper::toDto);
    }

    @Override
    public List<NotificationDto> getUserUnreadNotifications(Long userId) {
        User user = userRepository.getReferenceById(userId);
        return notificationRepository.findByRecipientAndIsReadFalseOrderByCreatedAtDesc(user)
                .stream()
                .map(notificationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public NotificationCountDto getUserNotificationCount(Long userId) {
        long total = notificationRepository.countByRecipientId(userId);
        long unread = notificationRepository.countUnreadByRecipient(userId);
        return new NotificationCountDto(total, unread);
    }

    @Override
    @Transactional
    public void markAsRead(Long notificationId, Long userId) {
        notificationRepository.markAsRead(notificationId, userId);
    }

    @Override
    @Transactional
    public void markAllAsRead(Long userId) {
        notificationRepository.markAllAsRead(userId);
    }

    @Override
    public void sendRealTimeNotification(Long userId, NotificationDto notification) {
        try {
            messagingTemplate.convertAndSendToUser(
                    userId.toString(),
                    "/queue/notifications",
                    notification
            );
        } catch (Exception e) {
            log.error("Failed to send real-time notification to user {}: {}", userId, e.getMessage());
            throw new NotificationException("Failed to send real-time notification", e);
        }
    }

    @Override
    @Transactional
    public void sendBulkNotifications(List<CreateNotificationDto> notifications) {
        if (notifications == null || notifications.isEmpty()) {
            throw new NotificationException("Notification list cannot be null or empty");
        }

        try {
            for (CreateNotificationDto dto : notifications) {
                createNotification(dto);
            }
        } catch (Exception e) {
            log.error("Failed to send bulk notifications: {}", e.getMessage());
            throw new NotificationException("Failed to send bulk notifications", e);
        }
    }

    @Override
    @Scheduled(cron = "0 0 3 * * ?")
    @Transactional
    public void cleanOldNotifications() {
        try {
            LocalDateTime cutoffDate = LocalDateTime.now().minusDays(30);
            int deletedCount = notificationRepository.deleteOldNotifications(cutoffDate);

            if (deletedCount > 0) {
                log.info("Cleaned up {} old notifications", deletedCount);
            }
        } catch (Exception e) {
            log.error("Failed to clean old notifications: {}", e.getMessage());
            throw new NotificationException("Failed to clean old notifications", e);
        }
    }
}