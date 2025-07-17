
package com.roze.smarthr.repository;

import com.roze.smarthr.entity.Employee;
import com.roze.smarthr.entity.Message;
import com.roze.smarthr.entity.MessageRecipientStatus;
import com.roze.smarthr.enums.MessageStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MessageRecipientStatusRepository extends JpaRepository<MessageRecipientStatus, Long> {
    Optional<MessageRecipientStatus> findByMessageAndRecipient(Message message, Employee recipient);

    long countByMessageAndStatus(Message message, MessageStatus status);
}