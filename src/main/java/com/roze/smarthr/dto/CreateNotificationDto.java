package com.roze.smarthr.dto;


import com.roze.smarthr.enums.NotificationPriority;
import com.roze.smarthr.enums.NotificationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateNotificationDto {
    @NotNull
    private Long recipientId;

    @NotBlank
    private String title;

    @NotBlank
    private String message;

    @NotNull
    private NotificationType type;

    @NotNull
    private NotificationPriority priority;

    private String actionUrl;
}