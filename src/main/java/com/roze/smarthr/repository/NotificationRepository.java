// NotificationRepository.java
package com.roze.smarthr.repository;

import com.roze.smarthr.entity.Notification;
import com.roze.smarthr.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Page<Notification> findByRecipientOrderByCreatedAtDesc(User user, Pageable pageable);

    List<Notification> findByRecipientAndIsReadFalseOrderByCreatedAtDesc(User user);

    @Query("SELECT COUNT(n) FROM Notification n WHERE n.recipient.id = :userId AND n.isRead = false")
    long countUnreadByRecipient(Long userId);

    @Transactional
    @Modifying
    @Query("UPDATE Notification n SET n.isRead = true WHERE n.id = :id AND n.recipient.id = :userId")
    int markAsRead(Long id, Long userId);

    @Transactional
    @Modifying
    @Query("UPDATE Notification n SET n.isRead = true WHERE n.recipient.id = :userId")
    int markAllAsRead(Long userId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Notification n WHERE n.createdAt < :cutoffDate")
    int deleteOldNotifications(LocalDateTime cutoffDate);

    long countByRecipientId(Long userId);
}