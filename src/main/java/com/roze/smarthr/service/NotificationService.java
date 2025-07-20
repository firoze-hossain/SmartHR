package com.roze.smarthr.service;

import com.roze.smarthr.dto.CreateNotificationDto;
import com.roze.smarthr.dto.NotificationCountDto;
import com.roze.smarthr.dto.NotificationDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    void sendInterviewScheduledNotification(String candidateEmail, String interviewerEmail,
                                            LocalDateTime scheduledDate, String meetingLink,String interviewType,String calendarEventId);

    void sendInterviewResultNotification(String candidateEmail, String result, String feedback);

    void sendOfferLetterNotification(String candidateEmail, String documentUrl);

    void sendOnboardingTaskAssignmentNotification(String assigneeEmail, String taskName, LocalDate deadline);

    void sendInterviewReminder(String candidateEmail, String interviewerEmail,
                               LocalDateTime scheduledDate, String meetingLink);

    void sendOfferStatusNotification(String issuerEmail, String candidateName, String newStatus);

    void sendOnboardingTaskCompletionNotification(String candidateEmail, String taskName);

    void sendOnboardingTaskReminder(String assigneeEmail, String taskName, LocalDate deadline);
}
