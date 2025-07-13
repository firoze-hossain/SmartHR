package com.roze.smarthr.controller;

import com.roze.smarthr.dto.BaseResponse;
import com.roze.smarthr.dto.CreateNotificationDto;
import com.roze.smarthr.dto.NotificationDto;
import com.roze.smarthr.entity.User;
import com.roze.smarthr.enums.NotificationPriority;
import com.roze.smarthr.enums.NotificationType;
import com.roze.smarthr.service.NotificationScheduler;
import com.roze.smarthr.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("test/notifications")
@RequiredArgsConstructor
@PreAuthorize("hasAnyAuthority('ADMIN', 'HR')") // Restrict to admin/HR for security
public class NotificationTestController {

    private final NotificationService notificationService;
    private final NotificationScheduler notificationScheduler;

    // 1. Push a test notification to yourself
    @PostMapping("/push_test")
    public ResponseEntity<BaseResponse<NotificationDto>> pushTestNotification(
            @AuthenticationPrincipal User user) {

        CreateNotificationDto testNotification = CreateNotificationDto.builder()
                .recipientId(user.getId())
                .title("Test Notification")
                .message("This is a test notification generated at " + LocalDateTime.now())
                .type(NotificationType.SYSTEM)
                .priority(NotificationPriority.MEDIUM)
                .build();

        NotificationDto notification = notificationService.createNotification(testNotification);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                "Test notification pushed successfully",
                notification
        ));
    }

    @PostMapping("/test_birthday")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<BaseResponse<String>> testBirthdayNotification() {
        notificationScheduler.sendBirthdayNotifications();
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                "Birthday notification test triggered",
                null
        ));
    }

    @PostMapping("/push_to_employee/{employeeId}")
    public ResponseEntity<BaseResponse<NotificationDto>> pushToEmployee(
            @PathVariable Long employeeId,
            @RequestParam(defaultValue = "Test Notification") String title,
            @RequestParam(defaultValue = "This is a test message") String message) {

        CreateNotificationDto dto = CreateNotificationDto.builder()
                .recipientId(employeeId)
                .title(title)
                .message(message)
                .type(NotificationType.SYSTEM)
                .priority(NotificationPriority.MEDIUM)
                .build();

        NotificationDto notification = notificationService.createNotification(dto);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                "Test notification pushed to employee " + employeeId,
                notification
        ));
    }

    // 2. Push custom notification to any user (for admin/HR testing)
    @PostMapping("/push_custom")
    public ResponseEntity<BaseResponse<NotificationDto>> pushCustomNotification(
            @RequestBody CreateNotificationDto createNotificationDto) {

        NotificationDto notification = notificationService.createNotification(createNotificationDto);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                "Custom notification pushed successfully",
                notification
        ));
    }

    // 3. Bulk push test notifications
    @PostMapping("/bulk_push/{count}")
    public ResponseEntity<BaseResponse<Map<String, Integer>>> pushBulkTestNotifications(
            @AuthenticationPrincipal User user,
            @PathVariable int count) {

        for (int i = 1; i <= count; i++) {
            CreateNotificationDto testNotification = CreateNotificationDto.builder()
                    .recipientId(user.getId())
                    .title("Test Notification #" + i)
                    .message("This is bulk test notification #" + i)
                    .type(NotificationType.SYSTEM)
                    .priority(NotificationPriority.LOW)
                    .build();
            notificationService.createNotification(testNotification);
        }

        Map<String, Integer> response = new HashMap<>();
        response.put("notificationsCreated", count);

        return ResponseEntity.ok(new BaseResponse<>(
                true,
                "Successfully created " + count + " test notifications",
                response
        ));
    }

    // 4. Get all notifications for the current user
    @GetMapping
    public ResponseEntity<BaseResponse<Page<NotificationDto>>> getMyNotifications(
            @AuthenticationPrincipal User user,
            @PageableDefault(size = 20) Pageable pageable) {

        Page<NotificationDto> notifications = notificationService.getUserNotifications(user.getId(), pageable);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                "Notifications retrieved successfully",
                notifications
        ));
    }

    // 5. Get unread notifications count
    @GetMapping("/unread_count")
    public ResponseEntity<BaseResponse<Map<String, Long>>> getUnreadCount(
            @AuthenticationPrincipal User user) {

        long unreadCount = notificationService.getUserNotificationCount(user.getId()).getUnread();

        Map<String, Long> response = new HashMap<>();
        response.put("unreadCount", unreadCount);

        return ResponseEntity.ok(new BaseResponse<>(
                true,
                "Unread count retrieved",
                response
        ));
    }

    // 6. Mark all as read
    @PostMapping("/mark_all_read")
    public ResponseEntity<BaseResponse<Void>> markAllAsRead(
            @AuthenticationPrincipal User user) {

        notificationService.markAllAsRead(user.getId());
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                "All notifications marked as read",
                null
        ));
    }
}