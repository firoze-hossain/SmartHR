
package com.roze.smarthr.repository;

import com.roze.smarthr.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    @Query("SELECT c FROM Conversation c JOIN c.participants p WHERE p.id = :employeeId ORDER BY c.updatedAt DESC")
    List<Conversation> findByParticipantId(@Param("employeeId") Long employeeId);

    @Query("SELECT c FROM Conversation c JOIN c.participants p WHERE p.id IN :participantIds " +
            "GROUP BY c HAVING COUNT(DISTINCT p.id) = :participantCount AND c.type = 'PRIVATE'")
    Optional<Conversation> findPrivateConversationBetweenParticipants(
            @Param("participantIds") List<Long> participantIds,
            @Param("participantCount") long participantCount);

    @Query("SELECT c FROM Conversation c WHERE c.id = :conversationId AND EXISTS " +
            "(SELECT p FROM c.participants p WHERE p.id = :employeeId)")
    Optional<Conversation> findByIdAndParticipant(
            @Param("conversationId") Long conversationId,
            @Param("employeeId") Long employeeId);
}