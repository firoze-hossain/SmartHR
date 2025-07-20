package com.roze.smarthr.service.implementation;

import com.roze.smarthr.dto.CreateNotificationDto;
import com.roze.smarthr.dto.NotificationCountDto;
import com.roze.smarthr.dto.NotificationDto;
import com.roze.smarthr.entity.Notification;
import com.roze.smarthr.entity.User;
import com.roze.smarthr.enums.NotificationPriority;
import com.roze.smarthr.enums.NotificationType;
import com.roze.smarthr.exception.NotificationException;
import com.roze.smarthr.exception.ResourceNotFoundException;
import com.roze.smarthr.mapper.NotificationMapper;
import com.roze.smarthr.repository.CandidateRepository;
import com.roze.smarthr.repository.NotificationRepository;
import com.roze.smarthr.repository.UserRepository;
import com.roze.smarthr.service.CalendarIntegrationService;
import com.roze.smarthr.service.NotificationService;
import com.roze.smarthr.utils.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private final EmailService emailService;
    private final CandidateRepository candidateRepository;
    private final CalendarIntegrationService calendarIntegrationService;

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

    @Override
    @Transactional
    public void sendInterviewScheduledNotification(String candidateEmail, String interviewerEmail,
                                                   LocalDateTime scheduledDate, String meetingLink, String interviewType,   String calendarEventId  ) {
        // Create notification for candidate
        User candidateUser = userRepository.findByEmail(candidateEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate user not found"));

        Notification candidateNotification = Notification.builder()
                .recipient(candidateUser)
                .title("Interview Scheduled")
                .message("Your interview has been scheduled for " + scheduledDate +
                        ". Meeting link: " + meetingLink)
                .type(NotificationType.INTERVIEW)
                .priority(NotificationPriority.HIGH)
                .actionUrl("/interviews")
                .build();
        notificationRepository.save(candidateNotification);

        // Create notification for interviewer
        User interviewerUser = userRepository.findByEmail(interviewerEmail)
                .orElseThrow(() -> new IllegalArgumentException("Interviewer user not found"));

        Notification interviewerNotification = Notification.builder()
                .recipient(interviewerUser)
                .title("Interview Scheduled")
                .message("You have an interview scheduled for " + scheduledDate +
                        " with a candidate. Meeting link: " + meetingLink)
                .type(NotificationType.INTERVIEW)
                .priority(NotificationPriority.HIGH)
                .actionUrl("/interviews")
                .build();
        notificationRepository.save(interviewerNotification);
        // Get calendar event link if available
        String calendarEventLink = calendarIntegrationService.getCalendarEventLink(calendarEventId);
        // Send emails
        emailService.sendInterviewScheduledEmail(candidateEmail, scheduledDate, meetingLink, interviewType, true,    calendarEventLink);
        emailService.sendInterviewScheduledEmail(interviewerEmail, scheduledDate, meetingLink, interviewType, false,    calendarEventLink);
    }

    @Override
    @Transactional
    public void sendInterviewResultNotification(String candidateEmail, String result, String feedback) {
        User candidateUser = userRepository.findByEmail(candidateEmail)
                .orElseThrow(() -> new IllegalArgumentException("Candidate user not found"));

        Notification notification = Notification.builder()
                .recipient(candidateUser)
                .title("Interview Result")
                .message("Your interview result: " + result +
                        (feedback != null ? "\nFeedback: " + feedback : ""))
                .type(NotificationType.INTERVIEW)
                .priority(NotificationPriority.MEDIUM)
                .actionUrl("/interviews")
                .build();
        notificationRepository.save(notification);

        emailService.sendInterviewResultEmail(candidateEmail, result, feedback);
    }

    @Override
    @Transactional
    public void sendOfferLetterNotification(String candidateEmail, String documentUrl) {
        User candidateUser = userRepository.findByEmail(candidateEmail)
                .orElseThrow(() -> new IllegalArgumentException("Candidate user not found"));

        Notification notification = Notification.builder()
                .recipient(candidateUser)
                .title("Offer Letter Received")
                .message("You have received an offer letter. Please review and respond.")
                .type(NotificationType.OFFER)
                .priority(NotificationPriority.HIGH)
                .actionUrl("/offer-letters")
                .build();
        notificationRepository.save(notification);

        emailService.sendOfferLetterEmail(candidateEmail, documentUrl);
    }

    @Override
    @Transactional
    public void sendOnboardingTaskAssignmentNotification(String assigneeEmail, String taskName, LocalDate deadline) {
        User assigneeUser = userRepository.findByEmail(assigneeEmail)
                .orElseThrow(() -> new IllegalArgumentException("Assignee user not found"));

        Notification notification = Notification.builder()
                .recipient(assigneeUser)
                .title("New Onboarding Task Assigned")
                .message("You have been assigned a new onboarding task: " + taskName +
                        " with deadline: " + deadline)
                .type(NotificationType.ONBOARDING)
                .priority(NotificationPriority.MEDIUM)
                .actionUrl("/onboarding-tasks")
                .build();
        notificationRepository.save(notification);

        emailService.sendOnboardingTaskAssignmentEmail(assigneeEmail, taskName, deadline);
    }

    @Override
    @Transactional
    public void sendInterviewReminder(String candidateEmail, String interviewerEmail,
                                      LocalDateTime scheduledDate, String meetingLink) {
        // Create reminder notification for candidate
        User candidateUser = userRepository.findByEmail(candidateEmail)
                .orElseThrow(() -> new IllegalArgumentException("Candidate user not found"));

        Notification candidateNotification = Notification.builder()
                .recipient(candidateUser)
                .title("Interview Reminder")
                .message("Reminder: Your interview is scheduled in 1 hour at " + scheduledDate +
                        ". Meeting link: " + meetingLink)
                .type(NotificationType.INTERVIEW)
                .priority(NotificationPriority.HIGH)
                .actionUrl("/interviews")
                .build();
        notificationRepository.save(candidateNotification);

        // Create reminder notification for interviewer
        User interviewerUser = userRepository.findByEmail(interviewerEmail)
                .orElseThrow(() -> new IllegalArgumentException("Interviewer user not found"));

        Notification interviewerNotification = Notification.builder()
                .recipient(interviewerUser)
                .title("Interview Reminder")
                .message("Reminder: You have an interview in 1 hour at " + scheduledDate +
                        " with a candidate. Meeting link: " + meetingLink)
                .type(NotificationType.INTERVIEW)
                .priority(NotificationPriority.HIGH)
                .actionUrl("/interviews")
                .build();
        notificationRepository.save(interviewerNotification);

        // Send reminder emails
        emailService.sendInterviewReminderEmail(candidateEmail, scheduledDate, meetingLink);
        emailService.sendInterviewReminderEmail(interviewerEmail, scheduledDate, meetingLink);
    }

    @Override
    @Transactional
    public void sendOfferStatusNotification(String issuerEmail, String candidateName, String newStatus) {
        User issuerUser = userRepository.findByEmail(issuerEmail)
                .orElseThrow(() -> new IllegalArgumentException("Issuer user not found"));

        Notification notification = Notification.builder()
                .recipient(issuerUser)
                .title("Offer Letter Status Update")
                .message("Offer letter status for candidate " + candidateName +
                        " has been updated to: " + newStatus)
                .type(NotificationType.OFFER)
                .priority(NotificationPriority.MEDIUM)
                .actionUrl("/offer-letters")
                .build();
        notificationRepository.save(notification);

        // Send email notification if needed
        emailService.sendOfferStatusUpdateEmail(issuerEmail, candidateName, newStatus);
    }

    @Override
    @Transactional
    public void sendOnboardingTaskCompletionNotification(String candidateEmail, String taskName) {
        User candidateUser = userRepository.findByEmail(candidateEmail)
                .orElseThrow(() -> new IllegalArgumentException("Candidate user not found"));

        // Notification for HR/Admin
        List<User> hrUsers = userRepository.findByRoleTitle("HR"); // Note: Using ROLE_HR to match Spring Security convention
        List<String> hrEmails = new ArrayList<>();

        if (hrUsers.isEmpty()) {
            log.warn("No HR users found to send onboarding task completion notification");
        } else {
            for (User hrUser : hrUsers) {
                Notification hrNotification = Notification.builder()
                        .recipient(hrUser)
                        .title("Onboarding Task Completed")
                        .message("Candidate " + candidateUser.getUsername() +
                                " has completed task: " + taskName)
                        .type(NotificationType.ONBOARDING)
                        .priority(NotificationPriority.MEDIUM)
                        .actionUrl("/onboarding-tasks")
                        .build();
                notificationRepository.save(hrNotification);
                hrEmails.add(hrUser.getEmail()); // Collect all HR emails
            }
        }

        // Notification for candidate
        Notification candidateNotification = Notification.builder()
                .recipient(candidateUser)
                .title("Task Completed")
                .message("You have successfully completed: " + taskName)
                .type(NotificationType.ONBOARDING)
                .priority(NotificationPriority.LOW)
                .actionUrl("/my-onboarding")
                .build();
        notificationRepository.save(candidateNotification);

        // Send emails if needed
        if (!hrEmails.isEmpty()) {
            // Send to all HR users
            for (String hrEmail : hrEmails) {
                emailService.sendOnboardingTaskCompletionEmail(hrEmail, candidateEmail, taskName);
            }
        }
        emailService.sendOnboardingTaskCompletionConfirmation(candidateEmail, taskName);
    }

    @Override
    @Transactional
    public void sendOnboardingTaskReminder(String assigneeEmail, String taskName, LocalDate deadline) {
        User assigneeUser = userRepository.findByEmail(assigneeEmail)
                .orElseThrow(() -> new IllegalArgumentException("Assignee user not found"));

        Notification notification = Notification.builder()
                .recipient(assigneeUser)
                .title("Onboarding Task Reminder")
                .message("Reminder: Task '" + taskName +
                        "' is overdue (deadline: " + deadline + ")")
                .type(NotificationType.ONBOARDING)
                .priority(NotificationPriority.HIGH)
                .actionUrl("/onboarding-tasks")
                .build();
        notificationRepository.save(notification);

        // Send email reminder
        emailService.sendOnboardingTaskReminderEmail(assigneeEmail, taskName, deadline);
    }
}