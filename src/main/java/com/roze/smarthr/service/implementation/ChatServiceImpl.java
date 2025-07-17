
package com.roze.smarthr.service.implementation;

import com.roze.smarthr.dto.*;
import com.roze.smarthr.entity.*;
import com.roze.smarthr.enums.ConversationType;
import com.roze.smarthr.enums.MessageStatus;
import com.roze.smarthr.exception.ResourceNotFoundException;
import com.roze.smarthr.mapper.ConversationMapper;
import com.roze.smarthr.mapper.MessageMapper;
import com.roze.smarthr.repository.ConversationRepository;
import com.roze.smarthr.repository.EmployeeRepository;
import com.roze.smarthr.repository.MessageRecipientStatusRepository;
import com.roze.smarthr.repository.MessageRepository;
import com.roze.smarthr.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final MessageRecipientStatusRepository statusRepository;
    private final EmployeeRepository employeeRepository;
    private final ConversationMapper conversationMapper;
    private final MessageMapper messageMapper;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    @Transactional
    public ConversationResponse createConversation(ConversationRequest request, User user) {
        Employee currentEmployee = employeeRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for user"));

        Set<Employee> participants = new HashSet<>();
        participants.add(currentEmployee);

        if (request.getParticipantIds() != null && !request.getParticipantIds().isEmpty()) {
            participants.addAll(employeeRepository.findAllById(request.getParticipantIds()));
        }

        if (request.getType() == ConversationType.PRIVATE && participants.size() != 2) {
            throw new IllegalArgumentException("Private conversations must have exactly 2 participants");
        }

        // Check if private conversation already exists
        if (request.getType() == ConversationType.PRIVATE) {
            List<Long> participantIds = participants.stream()
                    .map(Employee::getId)
                    .collect(Collectors.toList());

            Optional<Conversation> existingConversation = conversationRepository
                    .findPrivateConversationBetweenParticipants(participantIds, (long) participantIds.size());

            if (existingConversation.isPresent()) {
                return conversationMapper.toConversationResponse(existingConversation.get(), currentEmployee.getId());
            }
        }

        Conversation conversation = Conversation.builder()
                .title(request.getTitle())
                .type(request.getType())
                .participants(participants)
                .build();

        Conversation savedConversation = conversationRepository.save(conversation);
        return conversationMapper.toConversationResponse(savedConversation, currentEmployee.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConversationResponse> getUserConversations(User user) {
        Employee employee = employeeRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for user"));

        List<Conversation> conversations = conversationRepository.findByParticipantId(employee.getId());
        return conversations.stream()
                .map(conv -> conversationMapper.toConversationResponse(conv, employee.getId()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ConversationResponse getConversation(Long conversationId, User user) {
        Employee employee = employeeRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for user"));

        Conversation conversation = conversationRepository.findByIdAndParticipant(conversationId, employee.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Conversation not found"));

        return conversationMapper.toConversationResponse(conversation, employee.getId());
    }

    @Override
    @Transactional
    public MessageResponse sendMessage(MessageRequest request, User user) {
        Employee sender = employeeRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for user"));

        Conversation conversation = conversationRepository.findByIdAndParticipant(
                request.getConversationId(), sender.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Conversation not found"));

        // Create a new HashSet to avoid concurrent modification
        Set<Employee> participants = new HashSet<>(conversation.getParticipants());

        Message message = Message.builder()
                .conversation(conversation)
                .sender(sender)
                .content(request.getContent())
                .build();

        Message savedMessage = messageRepository.save(message);

        // Create recipient statuses
        Set<MessageRecipientStatus> statuses = new HashSet<>();
        for (Employee participant : participants) {
            if (!participant.getId().equals(sender.getId())) {
                statuses.add(MessageRecipientStatus.builder()
                        .message(savedMessage)
                        .recipient(participant)
                        .status(MessageStatus.SENT)
                        .build());
            }
        }

        if (!statuses.isEmpty()) {
            statusRepository.saveAll(statuses);
        }

        conversation.setUpdatedAt(LocalDateTime.now());
        conversationRepository.save(conversation);

        MessageResponse response = messageMapper.toMessageResponse(savedMessage);
        notifyNewMessage(response);

        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MessageResponse> getConversationMessages(Long conversationId, User user) {
        Employee employee = employeeRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for user"));

        conversationRepository.findByIdAndParticipant(conversationId, employee.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Conversation not found"));

        return messageRepository.findByConversationIdOrderBySentAtAsc(conversationId).stream()
                .map(messageMapper::toMessageResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateMessageStatus(Long messageId, MessageStatusUpdateRequest request, User user) {
        Employee employee = employeeRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for user"));

        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found"));

        conversationRepository.findByIdAndParticipant(message.getConversation().getId(), employee.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Conversation not found"));

        MessageRecipientStatus status = statusRepository.findByMessageAndRecipient(message, employee)
                .orElseThrow(() -> new ResourceNotFoundException("Message status not found"));

        status.setStatus(request.getStatus());
        if (request.getStatus() == MessageStatus.READ) {
            status.setReadAt(LocalDateTime.now());
        }

        statusRepository.save(status);

        // Update message status if all recipients have read it
        long unreadCount = statusRepository.countByMessageAndStatus(message, MessageStatus.SENT);
        if (unreadCount == 0) {
            message.setStatus(MessageStatus.READ);
            messageRepository.save(message);
        }

        notifyMessageStatusUpdate(messageId, request.getStatus());
    }

    @Transactional
    @Override
    public void notifyNewMessage(MessageResponse message) {
        messagingTemplate.convertAndSend("/topic/conversations/" + message.getConversationId() + "/messages", message);
    }

    @Transactional
    @Override
    public void notifyMessageStatusUpdate(Long messageId, MessageStatus status) {
        messagingTemplate.convertAndSend("/topic/messages/" + messageId + "/status", status);
    }
}