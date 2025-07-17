
package com.roze.smarthr.mapper;

import com.roze.smarthr.dto.ConversationResponse;
import com.roze.smarthr.entity.Conversation;
import com.roze.smarthr.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConversationMapper {
    private final MessageRepository messageRepository;
    private final EmployeeMapper employeeMapper;

    public ConversationResponse toConversationResponse(Conversation conversation, Long currentUserId) {
        return ConversationResponse.builder()
                .id(conversation.getId())
                .title(conversation.getTitle())
                .type(conversation.getType())
                .participants(employeeMapper.toEmployeeResponseSet(conversation.getParticipants()))
                .createdAt(conversation.getCreatedAt())
                .updatedAt(conversation.getUpdatedAt())
                .unreadCount(messageRepository.countUnreadMessages(conversation.getId(), currentUserId))
                .build();
    }
}