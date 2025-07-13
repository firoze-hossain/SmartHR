package com.roze.smarthr.controller;

import com.roze.smarthr.constant.GlobalMessage;
import com.roze.smarthr.dto.BaseResponse;
import com.roze.smarthr.dto.NotificationCountDto;
import com.roze.smarthr.dto.NotificationDto;
import com.roze.smarthr.entity.User;
import com.roze.smarthr.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<BaseResponse<Page<NotificationDto>>> getMyNotifications(
            @AuthenticationPrincipal User user,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<NotificationDto> notifications = notificationService.getUserNotifications(user.getId(), pageable);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.READ_SUCCESS,
                notifications
        ));
    }

    @GetMapping("/unread")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<BaseResponse<List<NotificationDto>>> getMyUnreadNotifications(
            @AuthenticationPrincipal User user) {
        List<NotificationDto> notifications = notificationService.getUserUnreadNotifications(user.getId());
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.READ_SUCCESS,
                notifications
        ));
    }

    @GetMapping("/count")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<BaseResponse<NotificationCountDto>> getMyNotificationCount(
            @AuthenticationPrincipal User user) {
        NotificationCountDto count = notificationService.getUserNotificationCount(user.getId());
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.READ_SUCCESS,
                count
        ));
    }

    @PutMapping("/{id}/read")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<BaseResponse<Void>> markAsRead(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        notificationService.markAsRead(id, user.getId());
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                "Notification marked as read",
                null
        ));
    }

    @PutMapping("/read_all")
    @PreAuthorize("isAuthenticated()")
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