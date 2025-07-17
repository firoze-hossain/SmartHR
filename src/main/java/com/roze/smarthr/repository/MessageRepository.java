
package com.roze.smarthr.repository;

import com.roze.smarthr.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByConversationIdOrderBySentAtAsc(Long conversationId);

    @Query("SELECT m FROM Message m WHERE m.conversation.id = :conversationId AND m.id > :lastMessageId ORDER BY m.sentAt ASC")
    List<Message> findNewMessages(
            @Param("conversationId") Long conversationId,
            @Param("lastMessageId") Long lastMessageId);

    @Query("SELECT COUNT(m) FROM Message m JOIN m.recipientStatuses rs " +
            "WHERE m.conversation.id = :conversationId AND rs.recipient.id = :recipientId AND rs.status = 'SENT'")
    long countUnreadMessages(
            @Param("conversationId") Long conversationId,
            @Param("recipientId") Long recipientId);
}