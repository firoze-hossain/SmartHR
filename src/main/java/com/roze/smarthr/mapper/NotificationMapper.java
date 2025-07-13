package com.roze.smarthr.mapper;

import com.roze.smarthr.dto.CreateNotificationDto;
import com.roze.smarthr.dto.NotificationDto;
import com.roze.smarthr.entity.Notification;
import com.roze.smarthr.entity.User;
import com.roze.smarthr.exception.ResourceNotFoundException;
import com.roze.smarthr.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationMapper {
    private final UserRepository userRepository;

    public Notification toEntity(CreateNotificationDto dto) {
        User recipient = userRepository.findById(dto.getRecipientId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + dto.getRecipientId()));

        return Notification.builder()
                .recipient(recipient)
                .title(dto.getTitle())
                .message(dto.getMessage())
                .type(dto.getType())
                .priority(dto.getPriority())
                .actionUrl(dto.getActionUrl())
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public NotificationDto toDto(Notification notification) {
        return NotificationDto.builder()
                .id(notification.getId())
                .recipientId(notification.getRecipient().getId())
                .recipientName(getUserFullName(notification.getRecipient()))
                .title(notification.getTitle())
                .message(notification.getMessage())
                .createdAt(notification.getCreatedAt())
                .isRead(notification.isRead())
                .type(notification.getType())
                .priority(notification.getPriority())
                .actionUrl(notification.getActionUrl())
                .build();
    }

    private String getUserFullName(User user) {
        return user.getUsername();
    }

    // For bulk operations
    public List<Notification> toEntities(List<CreateNotificationDto> dtos) {
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    public List<NotificationDto> toDtos(List<Notification> notifications) {
        return notifications.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}