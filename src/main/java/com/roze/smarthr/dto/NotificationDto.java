
package com.roze.smarthr.dto;

import com.roze.smarthr.enums.NotificationPriority;
import com.roze.smarthr.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDto {
    private Long id;
    private Long recipientId;
    private String recipientName;
    private String title;
    private String message;
    private LocalDateTime createdAt;
    private boolean isRead;
    private NotificationType type;
    private NotificationPriority priority;
    private String actionUrl;
}





