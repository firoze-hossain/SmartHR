
package com.roze.smarthr.mapper;

import com.roze.smarthr.dto.MessageResponse;
import com.roze.smarthr.entity.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageMapper {
    private final EmployeeMapper employeeMapper;

    public MessageResponse toMessageResponse(Message message) {
        return MessageResponse.builder()
                .id(message.getId())
                .conversationId(message.getConversation().getId())
                .sender(employeeMapper.toDto(message.getSender()))
                .content(message.getContent())
                .sentAt(message.getSentAt())
                .status(message.getStatus())
                .build();
    }
}